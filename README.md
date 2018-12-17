How to run bots

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -cp /home/kktsang/ece457b-project/starterbot/bin bot.BotStarter" "java -cp /home/kktsang/ece457b-project/callbot/bin bot.BotStarter" 2>err.txt 1>out.txt

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -cp ../starterbot/bin bot.BotStarter" "java -cp ../callbot/bin bot.BotStarter" 2>err.txt 1>out.txt

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -cp ../starterbot/bin bot.BotStarter" "java -cp ../callbot/bin bot.BotStarter" 2>err.txt 1>out.txt

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -jar ../player/player.jar" "java -cp ../randombot/bin bot.BotStarter" 2>err.txt 1>out.txt

java -jar ../player/player.jar

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -jar ../fuzzybot/fuzzybot.jar" "java -cp ../randombot/bin bot.BotStarter" 2>err.txt 1>out.txt

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -jar ../evalbot/evalbot.jar" "java -jar ../fuzzybot/fuzzybot.jar" 2>err.txt 1>out.txt

java -cp bin com.theaigames.game.texasHoldem.TexasHoldem "java -jar ../evalbot/evalbot.jar" "java -cp ../randombot/bin bot.BotStarter" 2>err.txt 1>out.txt


Test Game

Settings hands_per_level 10
Settings starting_stack 2000
Settings your_bot player1
Match round 1
Match small_blind 10
Match big_blind 20
Match on_button player1
player1 stack 2000
player2 stack 2000
player1 post 15
player2 post 30
player1 hand [Qc,9h]
Match max_win_pot 30
Match amount_to_call 10
Action player1 10000
