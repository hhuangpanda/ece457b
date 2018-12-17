from pybrain.tools.shortcuts import buildNetwork
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.datasets import SupervisedDataSet
from pybrain.structure.modules import SigmoidLayer, SoftmaxLayer, TanhLayer
from pybrain.tools.customxml.networkwriter import NetworkWriter
from pybrain.tools.customxml.networkreader import NetworkReader
from math import sqrt
from os.path import isfile
import atexit

"""	Input:
		State: 1, 2, 3, 4, 5
		Cards Input: 0-1
		Risk Evaluation: 0-1
		Pot: 0-1
		If State 5:
			Earned/Lost: -1-1

	Output:
		Decision: 0-1
"""

neuralNetwork = 0
inputDataSet = []
outputData = 0
# pretrainData = [[1, 0.461538, sqrt(0.214178), 0.333333], 0.3,
# 				[1, 0.230769, sqrt(0.728507), 0.0], 0.3,
# 				[1, 0.326923, sqrt(0.493213), 0.25], 0.3,
# 				[1, 0.461538, sqrt(0.214178), 0.0], 0.3,
# 				[1, 0.461538, sqrt(0.214178), 0.25], 0.3,
# 				[1, 0.769231, sqrt(0.0181), 0.0], 0.3,
# 				[1, 0.134615, sqrt(0.909502), 0.25], 0.3,
# 				[1, 0.269231, sqrt(0.628959), 0.25], -0.35,
# 				[2, 0.090186, sqrt(0.628959), 0.0], -0.35,
# 				[3, 0.153846, sqrt(0.628959), 0.192308], -0.35,
# 				[4, 0.161804, sqrt(0.7), 0.108696], -0.35,
# 				[1, 0.211538, sqrt(0.764706), 0.0], -0.107692,
# 				[2, 0.095491, sqrt(0.4647), 0.0], -0.107692,
# 				[3, 0.114058, sqrt(0.5), 0.0], -0.107692,
# 				[4, 0.119363, sqrt(0.6), 0.0], -0.107692,
# 				[4, 0.119363, sqrt(0.6), 0.357143], -0.107692,
# 				[1, 0.269231, sqrt(0.628959), 0.25], -0.235294,
# 				[2, 0.087533, sqrt(0.728), 0.357143], -0.235294,
# 				[3, 0.116711, sqrt(0.828), 0.230769], -0.235294,
# 				[1, 0.538462, sqrt(0.105581), 0.25], 0.5,
# 				[2, 0.289125, sqrt(0.021259), 0.0], 0.5]

pretrainData = [[1, 0.461538, sqrt(0.214178)], 0.3,
				[1, 0.230769, sqrt(0.728507)], 0.3,
				[1, 0.326923, sqrt(0.493213)], 0.3,
				[1, 0.461538, sqrt(0.214178)], 0.3,
				[1, 0.461538, sqrt(0.214178)], 0.3,
				[1, 0.769231, sqrt(0.018100)], 0.3,
				[1, 0.134615, sqrt(0.909502)], 0.3,
				[1, 0.269231, sqrt(0.628959)], -0.35,
				[2, 0.090186, sqrt(0.628959)], -0.35,
				[3, 0.153846, sqrt(0.628959)], -0.35,
				[4, 0.161804, sqrt(0.700000)], -0.35,
				[1, 0.211538, sqrt(0.764706)], -0.107692,
				[2, 0.095491, sqrt(0.464700)], -0.107692,
				[3, 0.114058, sqrt(0.500000)], -0.107692,
				[4, 0.119363, sqrt(0.600000)], -0.107692,
				[4, 0.119363, sqrt(0.600000)], -0.107692,
				[1, 0.269231, sqrt(0.628959)], -0.235294,
				[2, 0.087533, sqrt(0.728000)], -0.235294,
				[3, 0.116711, sqrt(0.828000)], -0.235294,
				[1, 0.538462, sqrt(0.105581)], 0.5,
				[2, 0.289125, sqrt(0.021259)], 0.5]

def init():
	print("Intitializing")
	global neuralNetwork, inputDataSet, outputData, atexit
	atexit.register(pickleWeights)
	load = True if input("Load file? (y/n) ").lower() == "y" else False
	if load:
		filename = input("File name? (Please do not indicate the extension)\n") + ".xml"
		print("Loading", filename)
		if isfile(filename):
			neuralNetwork = NetworkReader.readFrom(filename)
		else:
			print("There is no such file. Creating a new network.")
	else:
		neuralNetwork = buildNetwork(3, 3, 3, 1, hiddenclass=TanhLayer, outclass=TanhLayer)
	pretrain = True if input("Pretrain with default set? (y/n) ").lower() == "y" else False
	if pretrain:
		preTrain()

def decisionMake():
	global neuralNetwork, inputDataSet, outputData
	while True:
		inputData = []
		try:
			inputData.append(int(input("State: ")))
		except:
			inputData.append(int(input("STATE!!!!!!!!!!!!!: ")))
		if inputData[0] == 5:
			break
		inputData.append(float(input("Cards Input: ")))
		inputData.append(float(input("Risk Evaluation: ")))
		#inputData.append(float(input("Pot: ")))
		outputData = neuralNetwork.activate(inputData)[0]
		print(outputData)
		inputDataSet.append(inputData)

def train():
	global neuralNetwork, inputDataSet, outputData
	if len(inputDataSet) == 1 and inputDataSet[0][2] < 0.78:
		outputData = 0.3 #Encourage exploration
	else:
		outputData = float(input("Money Earned? "))
		if inputDataSet[len(inputDataSet)-1][2] < 0.3 and outputData < 0:
			outputData = 0.3 #Encourage exploration if risk is low
	print("Training with", outputData)
	trainingSets = SupervisedDataSet(3, 1)
	for i in range(len(inputDataSet)):
		print(inputDataSet[i])
		trainingSets.addSample(inputDataSet[i], outputData)
	trainer = BackpropTrainer(neuralNetwork, trainingSets, learningrate = 0.7, momentum = 0.3)
	trainer.trainEpochs()
	del inputDataSet[:]
	NetworkWriter.writeToFile(neuralNetwork, "network.xml")
	print("Updating neural network")

def preTrain():
	global pretrainData
	i = 0
	trainingSets = SupervisedDataSet(3, 1)
	while i < len(pretrainData):
		inDataSam = pretrainData[i]
		i += 1
		outDataSam = pretrainData[i]
		i += 1
		trainingSets.addSample(inDataSam, outDataSam)
	trainer = BackpropTrainer(neuralNetwork, trainingSets, learningrate = 0.3, momentum = 0.3)
	for i in range(10):
		print("Training", str(i+1)+"th time")
		trainer.trainEpochs()

def pickleWeights():
	global neuralNetwork
	save = True if input("Save to file? (y/n) ").lower() == "y" else False
	if save:
		filename = input("File name? (Please do not indicate the extension)\n") + ".xml"
		print("Saving to", filename)
		NetworkWriter.writeToFile(neuralNetwork, filename)

def main():
	init()
	while True:
		decisionMake()
		train()

if __name__ == '__main__':
	#Build Newral Network
	main()
