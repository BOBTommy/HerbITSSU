from interfaces import HerbAprioriType


#This is python module used in HERB IT SSU Project
#Using Algorithm : Apriori
#Reference Book  : Machine Learning in Action
#Class : HerbApriori 

#Set Format
    #:

#Test Data Example

#dataSet = loadDataSet()
#print dataSet

#L, suppData = apriori(dataSet, minSupport=0.5)

#print suppData
#rules = generateRules(L, suppData, minConf=0.5)
#print "Test of frozen Set"
#listOfSet = list(L[1][1])
#print listOfSet


class HerbApriori(HerbAprioriType):

    Set = []
    rules = ""

    def __init__(self, Set = [[1,3,4], [2,3,5], [1,2,3,5], [2,5]]):
        self.Set = [[1,3,4], [2,3,5], [1,2,3,5], [2,5]]
        
    def loadDataSet(self):
        return [[1,3,4], [2,3,5], [1,2,3,5], [2,5]]

    def createC1(self, dataSet):
        C1 = []
        for transaction in dataSet:
            for item in transaction:
                if not [item] in C1:
                    C1.append([item])
        C1.sort()
        return map(frozenset,C1)

    def scanD(self,D, Ck, minSupport):
        ssCnt = {}
        for tid in D:
            for can in Ck:
                if can.issubset(tid):
                    if not ssCnt.has_key(can) : ssCnt[can] = 1
                    else: ssCnt[can] += 1
        numItems = float(len(D))
        retList = []
        supportData = {}
        for key in ssCnt:
            support = ssCnt[key] / numItems
            if support >= minSupport:
                retList.insert(0, key)
            supportData[key] = support
        return retList, supportData

    def aprioriGen(self,Lk, k):
        retList = []
        lenLk = len(Lk)
        for i in range(lenLk):
            for j in range(i+1, lenLk):
                L1 = list(Lk[i])[:k-2]; L2 = list(Lk[j])[:k-2]
                L1.sort(); L2.sort()
                if L1==L2:
                    retList.append(Lk[i] | Lk[j])
        return retList

    def apriori(self,dataSet, minSupport = 0.5):
        C1 = self.createC1(dataSet)
        D = map(set,dataSet)
        L1, supportData = self.scanD(D,C1,minSupport)
        L = [L1]
        k = 2
        while (len(L[k-2]) > 0):
            Ck = self.aprioriGen(L[k-2],k)
            Lk, supK = self.scanD(D,Ck,minSupport)
            supportData.update(supK)
            L.append(Lk)
            k+=1
        return L, supportData
        
    def generateRules(self,L, supportData, minConf= 0.7):
        bigRuleList = []
        for i in range(1,len(L)):
            for freqSet in L[i]:
                H1 = [frozenset([item]) for item in freqSet]
                if (i > 1):
                    self.rulesFromConseq(freqSet, H1, supportData, bigRuleList, minConf)
                else:
                    self.calcConf(freqSet, H1, supportData, bigRuleList, minConf)
        return bigRuleList

    def calcConf(self,freqSet, H, supportData, brl, minConf=0.7):
        prunedH = []
        for conseq in H:
            conf = supportData[freqSet]/supportData[freqSet-conseq]
            if conf >= minConf :
                #print freqSet-conseq, '-->', conseq, 'conf:', conf
                brl.append((freqSet-conseq, conseq, conf))
                prunedH.append(conseq)
        return prunedH

    def rulesFromConseq(self,freqSet, H, supportData, brl, minConf=0.7):
        m = len(H[0])
        if (len(freqSet) > (m+1)):
            Hmp1 = self.aprioriGen(H, m+1)
            Hmp1 = self.calcConf(freqSet, Hmp1, supportData, brl, minConf)
            if (len(Hmp1) > 1):
                self.rulesFromConseq(freqSet, Hmp1, supportData, brl, minConf)
                
    def getResult(self):
        self.Set = self.loadDataSet()
        
        print "#### Data Set is loaded####"
        #print self.Set
        
        L, suppData = self.apriori(self.Set,minSupport=0.5)
        print "#### Support Data is set####"
        #print suppData
        
        print "#### Result Rules are generated####"
        self.rules = self.generateRules(L,suppData,minConf=0.5)
        
    def getResultString(self):
        #return self.rules
        return str(self.rules)
        