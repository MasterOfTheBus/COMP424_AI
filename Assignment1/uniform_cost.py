import json
import urllib.request
import heapq
from pprint import pprint

url = "http://www.cs.mcgill.ca/~jpineau/comp424/Homework/brussels_metro_v2.json"
response = urllib.request.urlopen(url).read()
data = json.loads(response.decode("utf8"))

#pprint(data)
stations = {}
visited = {}
queue = [] # init a heap

#the start is line 3, Gare du Nord
for station in data["stations"]:
	stations[station["name"]] = station["neighbours"] # iterate now so we don't have to later
	if station["name"] == "Gare du Nord":
		for num in range(3, 5):
			topush = "Gare du Nord", num
			start = topush, None # save the parent node
			heapq.heappush(queue, (0, start))

		visited[station["name"]] = 0

stack = []

while (len(queue)):
	node = heapq.heappop(queue) # pop
	if node[1][0][0] == "Roi Baudouin": # terminate if reached the target
		stack.append(node)
		break
	
	for station in stations[node[1][0][0]]: # get the neighbors
		line_cost = (2 if station["line"] <= 3 else 1) # define the cost of each line
		station_cost = node[0] + line_cost
		if station["name"] not in visited or station_cost < visited[station["name"]]:
			topush = station["name"], station["line"]
			next = topush, node
			heapq.heappush(queue, (station_cost, next))
			visited[station["name"]] = station_cost

target = stack[len(stack)-1] # get the top of the stack
node = target[0], target[1][0]
stack.pop() # the first stack element isn't like the rest we'll put in so remove it first
stack.append(node)
while (target[1][1] is not None):
	target = target[1][1] # set the new value to the parent
	node = target[0], target[1][0]
	stack.append(node) # push the new value on top

pathcost = 0
while (len(stack)):
	top = stack.pop()
	print (top)
	pathcost = top[0]

print ("cost: " + str(pathcost))
