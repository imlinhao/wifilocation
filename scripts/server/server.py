import Queue,socket,threading,sys
from threading import Thread
from radiomap import Radiomap
#The Queue module implements multi-producer, multi-consumer queues
# working thread
class Worker(Thread):
    worker_count = 0
    radiomap = Radiomap('radiomap')
    timeout = 10
    def __init__( self, requestQ ):
        Thread.__init__( self )
        self.id = Worker.worker_count
        Worker.worker_count += 1
        self.setDaemon( True )
        self.requestQ = requestQ
        self.start()
    def run( self ):
        ''' the get-some-work, do-some-work main loop of worker threads '''
        print('worker:%d run' % self.id)
        while True:
            try:
                connection = self.requestQ.get()
                #serve the connection until timeout or client socket close
                while True:
                    try:
                        buf = connection.recv(1024) #the trail may be ,
                        #if buf == '':continue
                        apRssiList = buf.split(',') #so the last may be ''
                        if apRssiList[len(apRssiList)-1] == '':apRssiList.pop()
                        location = Worker.radiomap.horusItLocation(apRssiList)
                        print(apRssiList)
                        print('served by worker:%d' % self.id)
                        connection.send('location:%s\n' % location )
                    except socket.timeout:  
                        print 'time out'
                        connection.close()
                        break
                    except Exception,e:#client close socket
                        connection.close()
                        break
            except Queue.Empty:
                break
            except :
                print 'worker[%2d]' % self.id, sys.exc_info()[:2]
                raise #does raise break while? raise like return

if __name__ == '__main__':  
    requestQ = Queue.Queue() 
    workerCount = 20
    for i in range(workerCount):Worker(requestQ)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  
    sock.bind(('127.0.0.1', 8001))  
    sock.listen(5)  
    while True:  
        connection,address = sock.accept()  
        connection.settimeout(5)  
        requestQ.put(connection)
        #connection.close() 