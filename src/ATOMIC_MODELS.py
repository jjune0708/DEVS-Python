import sys
import math
import json
from abc import abstractmethod

from src.ENTITIES import ENTITIES

sys.path.append('D:/Git/DEVS-Python')

from src.MODELS import MODELS
from src.SIMULATORS import SIMULATORS
from src.PORT import PORT 
from src.CONTENT import CONTENT

class ATOMIC_MODELS(MODELS):
    """! ATOMIC_MODELS class.
    모델말자 모델�에�용�는 �수�의
    """

    def __init__(self):
        MODELS.__init__(self)
        self.processor = SIMULATORS()
        self.setProcessor(self.processor)
        self.processor.setDevsComponent(self)
    
        self.state = {}
        self.state["sigma"] = math.inf
        self.state["phase"] = "passive"

        self.ta = 0
        self.elapsed_time = 0        
    
    def setName(self, name):
        self.processor.setName(name)
        super().setName(name)

    def addState(self, key, value):
        self.state[key] = value
        
    def holdIn(self, phase, sigma):
        self.state["sigma"] = sigma
        self.state["phase"] = phase

    def Continue(self, e):
        """! 
        @fn         Continue
        @brief      ��태�이�수�서 �자 모델�행 중인�력�어�을 �재 �그마� 계산�는 �수
        @details    �재 �그�= �전 �그�- 경과�간

        @param e    elapsed_time(경과 �간)

        @author     �수�sumannam@gmail.com)
        @date       2021.05.09        

        @remarks    sigma가 �수�� �수 계산�라 결과 �일(�수�는 '.0'�하 �외)[2021.10.20; �수�
                    �전 �스 'self.state["sigma"] = self.state["sigma"] - e'�계산��나 "AttributeError: 'P' object has no attribute 'e'"가 발생�여 �시 변�로 계산롄달[2021.10.03; �수�
        
        """
        if self.state["sigma"] != math.inf:
            self.elapsed_time = e
            previous_sigma = self.decideNumberType(self.state["sigma"])
            current_sigma = previous_sigma - self.elapsed_time

            self.state["sigma"] = current_sigma
    
    def passviate(self):
        self.state["sigma"] = math.inf
        self.state["phase"] = "passive"
    
    def timeAdvancedFunc(self):
        self.ta = self.state["sigma"]
        return self.ta

    def modelTest(self, model):
        while True:
            param = [x for x in input(">>> ").split()]
            type = param[2]

            if type == "inject":
                port_name = param[3]
                value = param[4]
                elased_time = self.decideNumberType(param[5])

                self.sendInject(port_name, value, elased_time)
                send_result = self.getInjectResult(type)
            
            if type == "output?":
                output = CONTENT()
                output = self.outputFunc(self.state)
                send_result = self.getOutputResult(output)

            if type == "int-transition":
                self.internalTransitionFunc(self.state)
                send_result = self.getIntTransitionResult()

            if type == "quit":
                break

    def decideNumberType(self, time):
        """! 
        @fn         decideNumberType
        @brief      �태변�의 sigma가 �수�� �수�� 결정
                    (sigma가 �수� �수�력모두 �용경우 출력��이 �음)
        @details    �수 값에�수 값을 빼서 0�면 �수, 0�니멤수

        @param time    sigma

        @author     �수�sumannam@gmail.com)
        @date       2021.10.21
        """
        float_time = float(time)
        
        if float_time - int(float_time) == 0:
            return int(time)
        elif float_time - int(float_time) != 0:
            return float(time)
        else:
            return False

    def sendInject(self, port_name, value, time):
        content = CONTENT()
        content.setContent(port_name, value)

        self.externalTransitionFunc(self.state, time, content)

    def getInjectResult(self, type):
        state_list = []
        result = ""

        for s in self.state.values():
            temp_str = str(s)

            state_list.append(temp_str)

        state_str = ' '.join(state_list)

        if type == "inject":
            result = "state s = (" + state_str + ")"

        return result
        
    def getOutputResult(self, content):
        result = "y = " + content.port + " " + content.value
        return result;

    def getIntTransitionResult(self):
        state_list = []
        result = ""

        for s in self.state.values():
            temp_str = str(s)

            state_list.append(temp_str)

        state_str = ' '.join(state_list)
        result = "state s = (" + state_str + ")"

        return result

    # s: state, e: elased_time, x: content
    @abstractmethod
    def externalTransitionFunc(self, e, x):
        pass

    @abstractmethod
    def internalTransitionFunc(self):
        pass

    @abstractmethod
    def outputFunc(self):
        pass