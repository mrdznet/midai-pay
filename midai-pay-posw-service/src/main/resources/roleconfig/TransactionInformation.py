class TransactionInformation(): 
 
   def __init__(self):
       moble = ""
       money = 0
       cardNo = ""
       transcationTime = 0
       areaCode = ""
       
   def setMoble(self, moble): 
       self.moble = moble
       
   def setMoney(self, money): 
       self.money = money
       
   def setCardNo(self, cardNo): 
       self.cardNo = cardNo
       
   def setTranscationTime(self, transcationTime): 
       self.transcationTime = transcationTime
       
   def setAreaCode(self, areaCode): 
       self.areaCode = areaCode
       
   def getChannelCodes(self):
       if self.money >= 5000*100 : 
           return '301000000001'
       else :
           return '00000081'
       