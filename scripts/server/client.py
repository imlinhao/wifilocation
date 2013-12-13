if __name__ == '__main__':  
    import socket,time  
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  
    sock.connect(('127.0.0.1', 8001))  
    while True:
        try:
            sock.send('00:b0:0c:42:8f:68,-65,00:25:86:3c:0d:b2,-67,94:0c:6d:49:a2:90,-77,f0:7d:68:91:93:00,-80,00:b0:0c:42:77:70,-83,38:83:45:53:6d:98,-84,00:b0:0c:0f:cd:98,-84')  
            print sock.recv(1024) 
            time.sleep(2)  
            sock.send('00:b0:0c:42:8f:68,-65,00:25:86:3c:0d:b2,-67,94:0c:6d:49:a2:90,-77,f0:7d:68:91:93:00,-80,00:b0:0c:42:77:70,-83,38:83:45:53:6d:98,-84,00:b0:0c:0f:cd:98,-84')  
            print sock.recv(1024)
            time.sleep(3)  
            sock.send('00:b0:0c:42:8f:68,-65,00:25:86:3c:0d:b2,-67,94:0c:6d:49:a2:90,-77,f0:7d:68:91:93:00,-80,00:b0:0c:42:77:70,-83,38:83:45:53:6d:98,-84,00:b0:0c:0f:cd:98,-84')  
            print sock.recv(1024) 
            time.sleep(3) 
        except Exception,e:  
            print e
            break
            sock.close() 
    
#test:for /l %i in (1,1,50) do python client.py