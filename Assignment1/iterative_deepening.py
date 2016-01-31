import json
import urllib.request
from pprint import pprint

url = "http://www.cs.mcgill.ca/~jpineau/comp424/Homework/brussels_metro_v2.json"
response = urllib.request.urlopen(url).read()
data = json.loads(response.decode("utf8"))

#pprint(data)
depth = 0
solved = False

tracestack = []

while not solved:
	stations = {}
	visited = set()
	stack = []

	#the start is line 3, Gare du Nord
	for station in data["stations"]:
		stations[station["name"]] = station["neighbours"] # iterate now so we don't have to later
		if station["name"] == "Gare du Nord":
			for num in range(0, 2):
				topush = {}
				topush["name"] = "Gare du Nord"
				topush["line"] = 4 - num # remember to start with line 3
				start = topush, None, 0 # save the parent node and depth
				stack.append(start)

			visit = station["name"], 3 # make sure to put in the line too
			visited.add(visit)
			visit = station["name"], 4
			visited.add(visit)

	tracestack = []

	while (len(stack)):
		node = stack.pop() # pop
		if node[0]["name"] == "Roi Baudouin": # terminate if reached the target
			tracestack.append(node)
			solved = True
			break
	
		node_depth = node[2] + 1

		if node_depth < depth:
			tempstack = [] # use a temporary stack so that the searching order is as specified
			for station in stations[node[0]["name"]]: # get the neighbors
				visit = station["name"], station["line"]
				if visit not in visited:
					next = station, node, node_depth
					tempstack.append(next)
					visited.add(visit)
			while (len(tempstack)):
				stack.append(tempstack.pop())

	depth += 1 # increment the depth

target = tracestack[len(tracestack)-1] # get the top of the tracestack
tracestack.pop() # the first tracestack element isn't like the rest we'll put in so remove it first
tracestack.append(target[0])
while (target[1] is not None):
	target = target[1] # set the new value to the parent
	tracestack.append(target[0]) # push the new value on top

cost = 0
first = True
while (len(tracestack)):
	node = tracestack.pop()
	print (node)
	if first:
		first = False
	else:
		if node["line"] <= 3:
			cost += 2
		else:
			cost += 1

print ("cost: " + str(cost))
print ("depth: " + str(depth))