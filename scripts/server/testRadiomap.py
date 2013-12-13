import radiomap
rm = radiomap.Radiomap('radiomap')
buf = '00:b0:0c:42:8f:68,-65,00:25:86:3c:0d:b2,-67'
apRssiList = buf.split(',')
.horusItLocation(apRssiList)