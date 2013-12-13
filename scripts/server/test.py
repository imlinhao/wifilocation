#!/usr/bin/python
import string
class RadiomapItem:
    location = ''
    apcount = 0
    aplist = []
    def __init__(self):
        self.location = ''
        self.apcount = 0
        self.aplist = [] #need init or all object will use the same aplist to append

def loadRadiomap(path):
    radiomap = []
    f = open(path)  
    for line in f.readlines():
        strlist = line.split(',')
        radiomapItem = RadiomapItem() #cannot place before for
        radiomapItem.location = strlist[0]
        radiomapItem.apcount = string.atoi(strlist[1])
        if 0==radiomapItem.apcount:
            print("no ap in the range")  
        for i in range(0,radiomapItem.apcount):
            radiomapItem.aplist.append(strlist[2+i*3])
            radiomapItem.aplist.append(strlist[4+i*3])
        radiomap.append(radiomapItem)
    return radiomap

def loadTest(path):
    test = []
    f = open(path)
    for line in f.readlines():
        strlist = line.split(',')
        radiomapItem = RadiomapItem()
        radiomapItem.location = strlist[0]
        radiomapItem.apcount = string.atoi(strlist[1])
        if 0==radiomapItem.apcount:
            print("no ap in the range")  
        selApcount = 0
        if 3>radiomapItem.apcount:
            selApcount = radiomapItem.apcount
        else:
            selApcount = 3
        for i in range(0,selApcount):
            radiomapItem.aplist.append(strlist[2+i*3])
            radiomapItem.aplist.append(strlist[4+i*3])
        test.append(radiomapItem)
    return test

radiomap = loadRadiomap('radiomap')
test = loadTest('test')
for testItem in test:
    nn = ''
    ld = -1 #least distance
    for i in range(len(radiomap)):
        inRadiomapItem = False
        sumd = 0
        for j in range(0,len(testItem.aplist),2):
            bssid = testItem.aplist[j]
            if bssid in radiomap[i].aplist:
                sumd = sumd + ( string.atoi(testItem.aplist[j+1]) - string.atoi(radiomap[i].aplist[radiomap[i].aplist.index(bssid)+1]) )**2
                inRadiomapItem = True
            else:
                inRadiomapItem = False
                break
        if True==inRadiomapItem:
            if -1==ld or ld > sumd:
                ld = sumd
                nn = radiomap[i].location
    print('testlocation:%s' % testItem.location)
    print('nearest neighbor:%s' % nn)
    print('least distance:%d\n' % ld)

                
            
        
        

    
