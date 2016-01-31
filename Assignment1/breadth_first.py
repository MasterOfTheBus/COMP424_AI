import json
import urllib.request
from collections import deque
from pprint import pprint

url = "http://www.cs.mcgill.ca/~jpineau/comp424/Homework/brussels_metro_v2.json"
response = urllib.request.urlopen(url).read()
data = json.loads(response.decode("utf8"))

#pprint(data)
stations = {}
visited = set()
queue = deque()

#the start is line 3, Gare du Nord
for station in data["stations"]:
	stations[station["name"]] = station["neighbours"] # iterate now so we don't have to later
	if station["name"] == "Gare du Nord":
		for num in range(3, 5):
			topush = {}
			topush["name"] = "Gare du Nord"
			topush["line"] = num
			start = topush, None # save the parent node
			queue.append(start)

		visit = station["name"], 3 # make sure to put in the line too
		visited.add(visit)
		visit = station["name"], 4
		visited.add(visit)

stack = []

while (len(queue)):
	node = queue.popleft() # pop
	if node[0]["name"] == "Roi Baudouin": # terminate if reached the target
		stack.append(node)
		break
	
	for station in stations[node[0]["name"]]: # get the neighbors
		visit = station["name"], station["line"]
		if visit not in visited:
			next = station, node
			queue.append(next)
			visited.add(visit)

target = stack[len(stack)-1] # get the top of the stack
stack.pop() # the first stack element isn't like the rest we'll put in so remove it first
stack.append(target[0])
while (target[1] is not None):
	target = target[1] # set the new value to the parent
	stack.append(target[0]) # push the new value on top

cost = 0
first = True
while (len(stack)):
	node = stack.pop()
	print (node)
	if first:
		first = False
	else:
		if node["line"] <= 3:
			cost += 2
		else:
			cost += 1

print ("cost: " + str(cost))
