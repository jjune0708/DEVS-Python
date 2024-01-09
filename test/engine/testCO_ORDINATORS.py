import os
import sys
import unittest

import src.util as util

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
import config

from src.MESSAGE import MESSAGE
from src.CONTENT import CONTENT
from src.CO_ORDINATORS import CO_ORDINATORS

from projects.simparc.coupbase.EF import EF
from projects.simparc.mbase.GENR import GENR

class testCO_ORDINATORS(unittest.TestCase):
    """
    �래�는 ROOT_CO_ORDINATORS �래�� �스�합�다.
    """

    def setUp(self):
        """
        �스�경�정�니
        """
        self.ef = EF()
        self.ef.initialize()

    def testInitialize(self):
        """
        모델 초기�� �스�합�다.

        �수모델 초기�의 최소 �간�스�합�다.
        EF-P최소 Sigma �간검�합�다.

        :�성 �수�sumannam@gmail.com)
        :�성 2024.01.04

        :TDD: 
        :�션: https://www.notion.so/modsim-devs/TDD-c80a15fcb34c40319b7a4e3d9b0211a7?pvs=4
        """
        time_list = list(self.ef.processor.processor_time.values())
        
        assert time_list == [0, 10]
        
    def testWhenReceiveStar(self):
        """
        모델whenReceiveStar �수륌스�합�다.

        �수초기모델whenReceiveStar �수륌스�합�다.
        'Star' 메시지류신�의 �음 �간�록 베이�� 같�지 검�합�다.

        :�성 �수�sumannam@gmail.com)
        :�성 2024.01.04
        """
        
        star_msg = MESSAGE()
        star_msg.setRootStar('Star', 0)
        self.ef.processor.whenReceiveStar(star_msg)
        
        time_next = self.ef.processor.getTimeOfNextEvent()
        
        assert time_next == 3
        
    def testWhenReceiveY(self):
        """
        모델whenReceiveY �수륌스�합�다.

        �수초기모델whenReceiveY �수륌스�합�다.
        'Y' 메시지류신�의 �음 �간�록 베이�� 같�지 검�합�다.

        :�성 �수�
        :�성 2024.01.04
        """
        util.UNITEST_METHOD = 'testWhenReceiveY'
        
        self.genr = GENR()       
        input_message = MESSAGE()
        input_message.setExt('Y', self.genr, 0)

        input_content = CONTENT()
        input_content.setContent("out", "TEST-1")
        input_message.addContent(input_content)        
        
        current_path = os.path.dirname(os.path.abspath(os.path.dirname(__file__)))
        parent_path = os.path.abspath(os.path.join(current_path, os.pardir))

        log_file = parent_path + "\\" + "sim_msg_log.txt"
                                       
        # �일존재�는 경우 ��
        if os.path.isfile(log_file):
            os.remove(log_file)
        else:
            print("Error: {} �일존재�� �습�다.".format(log_file))

        self.ef.processor.whenReceiveY(input_message)
        
        
        
    def testWhenReceiveX(self):
        self.genr = GENR()

        input_message = MESSAGE()
        input_message.setExt('X', self.genr, 0)
        