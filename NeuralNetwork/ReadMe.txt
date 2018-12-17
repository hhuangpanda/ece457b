How to run the neural network:

Please have pip3 installed.

For Linux:
	Have virtualenv installed
	Go to /NeuralNetwork folder
	Open requirement.txt and remove the line containing "pybrain". Then save the file and close it.
	Type the following commands in order:
		virtualenv -p python3.4 .
		source bin/activate
		pip3 install -U pip
		pip3 -r requirement.txt
		pip3 install https://github.com/pybrain/pybrain/archive/0.3.3.zip

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -cp ../starterbot/bin bot.BotStarter" "java -jar ../NeuralNetwork/NeuralNetwork.jar" 2>err.txt 1>out.txt &