import math

def function(x):
	return 0 if x == 0 else math.sin(x * x / 2) / math.sqrt(x)

# assuming that we find the max value
starting_points = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
step_size = []
for num in range(1, 11):
	step_size.append(num / 100)

for start in starting_points:
	for step in step_size:
		num_steps = 0
		current_x = start
		current_max = (0 if start == 0 else function(start))
		max_index = current_x

		left = function(current_x - step if current_x - step >= 0 else 0)
		right = function(current_x + step if current_x + step <= 10 else 10)
		opt = left if left > right else right
		current_x = current_x - step if left > right else current_x + step

		while (current_max < opt):
			num_steps += 1
			current_max = opt
			max_index = current_x

			left = function(current_x - step if current_x - step >= 0 else 0)
			right = function(current_x + step if current_x + step <= 10 else 10)
			opt = left if left > right else right
			current_x = current_x - step if left > right else current_x + step

		# start, step size, x, y, steps
		print(str(start) + ", " + str(step) + ", " + str(max_index) + ", " + str(current_max) + ", " + str(num_steps))