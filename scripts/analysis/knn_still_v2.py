#-*- coding: utf-8 -*-
import re
import matplotlib.pyplot as plt
from matplotlib.font_manager import FontProperties
import numpy as np
import math

font = FontProperties(fname=r"c:\windows\fonts\simsun.ttc", size=14)
DATA_ROOT = "../datasets/fingerprint_db/hao_nexus7"
STILLTIME_FN = DATA_ROOT+"/2013_11_29_21_26_4_offline/stilltime.txt"
DB_FN = DATA_ROOT+"/2013_11_29_21_26_4_offline/wifi.txt"
ONLINE_STILLTIME_FN = DATA_ROOT+"/2013_11_29_21_26_4_online/stilltime.txt"
QUERY_FN = DATA_ROOT+"/2013_11_29_21_26_4_online/wifi.txt"
loc_list = range(0,51)

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

db_list = []
f_db = open(DB_FN)
prev_loc_index = 0
ap_rssilist_dict = {}
while True:
	line = f_db.readline()
	if not line : break
	time = int(re.search("^(\d*);",line).group(1))
	loc_index = search_locindex(time,stilltime_list)
	if(loc_index<0): continue
	if(loc_index==prev_loc_index):
		ap_rssi_all = re.findall(',(.*?,..:..:..:..:..:..),.*?(-\d*);',line)
		for ap,rssi in ap_rssi_all:
			if(ap not in ap_rssilist_dict.keys()): ap_rssilist_dict[ap] = [int(rssi)]
			else: ap_rssilist_dict[ap].append(int(rssi))
	else: 
		prev_loc_index=loc_index
		db_entry = {}
		for ap in ap_rssilist_dict.keys():
			db_entry[ap] = [np.mean(ap_rssilist_dict[ap]),np.std(ap_rssilist_dict[ap])]
		db_list.append(db_entry)
		ap_rssilist_dict = {}
#the db_entry for last location, TODO:sames not optimal for program
db_entry = {}
for ap in ap_rssilist_dict.keys():
	db_entry[ap] = [np.mean(ap_rssilist_dict[ap]),np.std(ap_rssilist_dict[ap])]
db_list.append(db_entry)
ap_rssilist_dict = {}
f_db.close()

#print(db_list)
#print(len(db_list))
online_stilltime_list = []
f_online_stilltime = open(ONLINE_STILLTIME_FN)
while True:
	line = f_online_stilltime.readline()
	if not line: break
	online_stilltime_list.append(int(line))
f_online_stilltime.close()

"""
query_entry format {ap1:rssi,...,apn:rssi}
db_entry format {AP1:[rssi_mean,rssi_std],...,APN:[rssi_mean,rssi_std]}
"""
def distance(query_entry, db_entry):
	query_entry_aps = query_entry.keys()
	db_entry_aps = db_entry.keys()
	ap_set = set(query_entry_aps+db_entry_aps)
	sum = 0
	for ap in ap_set:
		query_rssi = -100; db_rssi = -100
		if ap in query_entry_aps: query_rssi = query_entry[ap]
		if ap in db_entry_aps: 
			db_rssi = db_entry[ap][0]
		sum += (query_rssi-db_rssi)**2
	return math.sqrt(sum)	

locindex = 0
query_list = []
err_distance_list = []
f_query = open(QUERY_FN)
while True:
	distance_list = []
	line = f_query.readline()
	if not line: break
	time = int(re.search("^(\d*);",line).group(1))
	if(time>online_stilltime_list[2*locindex]):
		query_entry = {}
		ap_rssi_all = re.findall(',(.*?,..:..:..:..:..:..),.*?(-\d*);',line)
		for ap,rssi in ap_rssi_all:
			query_entry[ap] = int(rssi)
		query_list.append(query_entry)
		for db_entry in db_list:
			distance_list.append(distance(query_entry,db_entry))
		cal_loc = loc_list[distance_list.index(min(distance_list))]
		real_loc = loc_list[locindex]
		err_distance_list.append(abs(cal_loc-real_loc))
		#print("cal loc: %d" % cal_loc)
		#print("real loc:%d" % real_loc)
		#print("error distance:%d" % abs(cal_loc-real_loc))
		#print("rssi distance:%d" % min(distance_list))

		locindex += 1
		if(len(loc_list)==locindex): break
f_query.close()

zero_count = err_distance_list.count(0)
print(zero_count)
zero_count_percentage = zero_count*1.0/len(err_distance_list)
bins=np.linspace(0,10,11)
hist,bin_edges = np.histogram(err_distance_list,bins=bins)
print(hist)
pdf = 1.0*hist/len(err_distance_list)
cdf = np.cumsum(pdf)
print(cdf)
plt.plot(bins,np.append([zero_count_percentage],cdf),'ko-')
plt.xlabel(u'定位误差(m)',fontproperties=font)
plt.ylabel(u'累积误差概率',fontproperties=font)
plt.show();
