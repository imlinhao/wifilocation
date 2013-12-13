import re
import math

DATA_ROOT = "../datasets/fingerprint_db/liang_mi"
STILLTIME_FN = DATA_ROOT+"/2013_10_26_20_22_9/stilltime.txt"
DB_FN = DATA_ROOT+"/2013_10_26_20_22_9/wifi.txt"
ONLINE_MOVETIME_FN = DATA_ROOT+"/2013_10_26_20_35_14/movetime.txt"
QUERY_FN = DATA_ROOT+"/2013_10_26_20_35_14/wifi.txt"
loc_list = [1,2,3,4,5]
ap_list = ['ACM,f4:ec:38:2e:6f:ac','minruigao-PC-68140,4c:eb:42:6d:af:84','TP-LINK_534E20,78:a1:06:53:4e:20','hi_Camp,d8:5d:4c:1b:90:6a']

stilltime_list = []
f_stilltime = open(STILLTIME_FN)
while True:
	line = f_stilltime.readline()
	if not line: break
	stilltime_list.append(int(line))
f_stilltime.close()

def search_locindex(time, timelist):
	#TODO:use binary search
	if time < timelist[0]: return -1
	if time > timelist[-1]: return -2
	for i in range(0,len(timelist)/2):
		if timelist[2*i]<time<timelist[2*i+1]:
			return i

dump_loc_list = []
db_list = []
f_db = open(DB_FN)
while True:
	line = f_db.readline()
	if not line : break
	time = int(re.search("^(\d*);",line).group(1))
	loc_index = search_locindex(time,stilltime_list)
	if(loc_index<0): continue
	dump_loc_list.append(loc_list[loc_index])
	db_entry = []
	for ap in ap_list:
		rssi_search = re.search(ap+",(-\d+?);",line)
		if rssi_search:
			db_entry.append(int(rssi_search.group(1)))
		else:
			db_entry.append(-100)
	db_list.append(db_entry)
f_db.close()

#print(len(dump_loc_list))
#print(len(dump_loc_list))
#print(dump_loc_list)
#print(db_list)

online_movetime_list = []
f_online_movetime = open(ONLINE_MOVETIME_FN)
while True:
	line = f_online_movetime.readline()
	if not line: break
	online_movetime_list.append(int(line))
f_online_movetime.close()

def distance(query_entry, db_entry):
	sum = 0
	for i in range(0,len(query_entry)):
		sum += (query_entry[i]-db_entry[i])**2
	return math.sqrt(sum)	

locindex = 0
query_list = []
f_query = open(QUERY_FN)
while True:
	distance_list = []
	line = f_query.readline()
	if not line: break
	time = int(re.search("^(\d*);",line).group(1))
	if(time>online_movetime_list[locindex]):
		query_entry = []
		for ap in ap_list:
			rssi_search = re.search(ap+",(-\d+?);",line)
			if rssi_search:
				query_entry.append(int(rssi_search.group(1)))
			else:
				query_entry.append(-100)
		query_list.append(query_entry)
		for db_entry in db_list:
			distance_list.append(distance(db_entry,query_entry))
		cal_loc = dump_loc_list[distance_list.index(min(distance_list))]
		real_loc = loc_list[locindex]
		print("cal loc: %d" % cal_loc)
		print("real loc:%d" % real_loc)
		print("distance:%d" % abs(cal_loc-real_loc))
		print("rssi distance:%d" % min(distance_list))

		locindex += 1
		if(len(loc_list)==locindex): break
f_query.close()

