import sys
import unittest

sys.path.append('D:/Git/DEVS-Python')

from projects.simparc.EF_P import EF_P
from src.CONTENT import CONTENT

class Test_tesPModelTest(unittest.TestCase):
    def setUp(self):
        self.ef_p = EF_P()
    
    def testAddModels(self):
        class_list = self.ef_p.getModels()
        model_list = []

        for model in class_list:
            model_list.append(model.__class__.__name__)

        assert model_list == ['P', 'EF']
    
    def testAddInteralCoupling(self):
        coupling_list = self.ef_p.getInternalCoupling()
        
        for coupling in coupling_list:
            print(coupling)


if __name__ == '__main__':
    unittest.main()