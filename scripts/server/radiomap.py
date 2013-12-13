import string
from scipy import stats
class RadiomapItem:
    location = ''
    apcount = 0
    apRssiList = []
    def __init__(self):
        self.location = ''
        self.apcount = 0
        self.apRssiList = [] #need init or all object will use the same apRssiList to append

class Radiomap(list):
    def __init__(self,path):
        f = open(path)  
        for line in f.readlines():
            strlist = line.split(',')
            radiomapItem = RadiomapItem() #cannot place before for
            radiomapItem.location = strlist[0]
            if strlist[len(strlist)=='']:strlist.pop() #if the end of the line is ','
            
            #radiomapItem.apcount = string.atoi(strlist[1])
            radiomapItem.apcount = (len(strlist)-2)/4
            apRssiListStart = 2;
            if 0==radiomapItem.apcount:
                print("no ap in the range")  
            for i in range(0,radiomapItem.apcount):
                radiomapItem.apRssiList.append(strlist[apRssiListStart+i*4])   #ap bssid
                radiomapItem.apRssiList.append(float(strlist[apRssiListStart+2+i*4])) #rssi mean
                radiomapItem.apRssiList.append(float(strlist[apRssiListStart+3+i*4])) #standard deviation
            self.append(radiomapItem)
    def nnLocation(self,clientApRssiList):
        nn = ''
        ld = -1 #least distance
        try:
            for i in range(len(self)):
                inRadiomapItem = False
                sumd = 0
                x = 0
                #use first 3 ap to location
                if len(clientApRssiList)<=6:
                    x = len(apRssiList)
                else:
                    x = 6
                for j in range(0,x,2):
                    bssid = clientApRssiList[j]
                    if bssid in self[i].apRssiList:
                        sumd = sumd + ( string.atoi(clientApRssiList[j+1]) - string.atoi(self[i].apRssiList[self[i].apRssiList.index(bssid)+1]) )**2
                        inRadiomapItem = True
                    else:
                        inRadiomapItem = False
                        break
                if True==inRadiomapItem:
                    if -1==ld or ld > sumd:
                        ld = sumd
                        nn = self[i].location
        except Exception,e:
            print(e)
            nn = ''; ld = -1
            return nn,ld
        return nn,ld
    def horusItLocation(self,clientApRssiList):
        threshold = 0.1
        tmpApRssiList = []
        for i in range(0,len(clientApRssiList),2):
            ap_rssi = (clientApRssiList[i],string.atoi(clientApRssiList[i+1]))
            tmpApRssiList.append(ap_rssi)
        sortedApRssiList = sorted(tmpApRssiList,key=lambda x:x[1],reverse=True) #sort by rssi desend order
        candidateList = []
        for j in range(len(sortedApRssiList)):
            candidateList.append((j,0))
        for currentAp,currentRssi in sortedApRssiList:
            tmpCandidateList=[]
            for index,p in candidateList:
                if currentAp in self[index].apRssiList:
                    #calculate the prob
                    idx = self[index].apRssiList.index(currentAp)
                    mean = self[index].apRssiList[idx+1]
                    stdev = self[index].apRssiList[idx+2]
                    normdist = stats.norm(mean,stdev)
                    prob = normdist.cdf(currentRssi+0.5)-normdist.cdf(currentRssi-0.5)
                    tmpCandidateList.append((index,prob))
            if tmpCandidateList==None:
                continue
            newTmpCandidateList = sorted(tmpCandidateList,key=lambda x:x[1],reverse=True) #prob desend order
            if (newTmpCandidateList[0][1] - newTmpCandidateList[1][1])/newTmpCandidateList[0][1]>threshold:
                break
            else:
                candidateList = newTmpCandidateList
        location = self[candidateList[0][0]].location
        return location
    def myItLocation(self,clientApRssiList):
        location = ''
        prob = 0
