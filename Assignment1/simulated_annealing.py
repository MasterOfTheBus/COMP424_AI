import math
import random

def function(x):
	return 0 if x <= 0 else math.sin(x * x / 2) / math.sqrt(x)

temperatures = [50, 100, 500, 1000, 5000, 10000]
starting_points = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
step_size = [0.05, 0.06, 0.07, 0.08, 0.09, 0.1]

for start_temp in temperatures:
	for start in starting_points:
		temperature = start_temp
		steps = 0
		max_value = function(start)
		max_index = start
		while temperature > 0.001:
			steps += 1
			# create a random neighbor by selecting a random step size
			step_index = random.randint(0, len(step_size)-1)
			neighbor = max_index - step_size[step_index] if random.randint(0,1) else max_index + step_size[step_index]
			if neighbor > 10:
				neighbor = max_index - step_size[step_index]
			elif neighbor < 0:
				neighbor = max_index + step_size[step_index]

			value = function(neighbor)

			if value > max_value:
				max_value = value
				max_index = neighbor
			else:
				prob_accept = math.exp((value - max_value)/temperature)
				if (random.random() <= prob_accept):
					max_value = value
					max_index = neighbor

			temperature *= (start_temp - 1) / start_temp

		print(str(start_temp) + ", " + str(start) + ", " + str(max_index) + ", " + str(max_value) + ", " + str(steps))
		