import re

#WIFI_FILENAME = "../datasets/fingerprint_db/liang_mi/2013_11_29_22_31_31/wifi.txt"
WIFI_FILENAME = "../datasets/wardriving/bao_coolpad/2013_10_27_13_24_52/wifi.txt"
channel_list = ['2412','2417','2422','2427','2432','2437','2442','2447','2452','2457','2462','2467','2472','2484']
time_list = []
count_list = []
three_channel_count_list = []
all_channel_count_list = []
f_wifi = open(WIFI_FILENAME)
while True:
	count_in_fp_list = []
	line = f_wifi.readline()
	if not line : break
	for channel in channel_list:
		searched_time = re.search("^(\d*);",line)
		time = int(searched_time.group(1))
		time_list.append(time)
		found = re.findall(";"+channel+",",line) #TODO:will error when SSID have some pattern
		count_in_fp_list.append(len(found))
	count_list.append(count_in_fp_list)
	three_channel_count = count_in_fp_list[1-1]+count_in_fp_list[6-1]+count_in_fp_list[11-11]
	three_channel_count_list.append(three_channel_count)
	all_channel_count = sum(count_in_fp_list)
	all_channel_count_list.append(all_channel_count)
	#if(three_channel_count<4 and all_channel_count>=4):
	#TODO:DEBUG:three_channel_count should must be less than all_channel_count
	if(three_channel_count < all_channel_count):
		print(time);print(three_channel_count);print(all_channel_count)
f_wifi.close()

print(all_channel_count_list)
print(three_channel_count_list)
print(len(all_channel_count_list))
print(len(three_channel_count_list))
