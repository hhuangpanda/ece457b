package bot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import poker.Card;
import poker.PokerMove;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerGUIController implements Initializable {

    @FXML private ImageView matchCard1;
    @FXML private ImageView matchCard2;
    @FXML private ImageView matchCard3;
    @FXML private ImageView matchCard4;
    @FXML private ImageView matchCard5;

    List<ImageView> imageViews = null;

    @FXML private ImageView player1Card1;
    @FXML private ImageView player1Card2;
    @FXML private Text player1NameLabel;
    @FXML private Text player1ActionLabel;

    @FXML private ImageView player2Card1;
    @FXML private ImageView player2Card2;
    @FXML private Text player2NameLabel;
    @FXML private Text player2ActionLabel;

    @FXML private Text roundNumberLabel;
    @FXML private Text chipsPlayer1Label;
    @FXML private Text chipsPlayer2Label;
    @FXML private Text betPlayer1Label;
    @FXML private Text betPlayer2Label;
    @FXML private Text potLabel;

    @FXML private Button checkButton;
    @FXML private Button foldButton;
    @FXML private Button callButton;
    @FXML private Button raiseButton;

    @FXML private TextField raiseTextfield;

    private BotState state = null;
    private PokerMove lastMove = null;

    public void setState(BotState s)
    {
        state = s;
    }

    public void updateMatch(BotState s)
    {
        state = s;

        String path = getClass().getClassLoader().getResource("resources/images/").toString();
        System.out.println(path);

        roundNumberLabel.setText("Round "+state.getRound());
        chipsPlayer1Label.setText(""+state.getmyStack());
        chipsPlayer2Label.setText(""+state.getOpponentStack());
        betPlayer1Label.setText(""+state.getCurrentBet());
        betPlayer2Label.setText(""+state.getAmountToCall());
        potLabel.setText(""+state.getPot());

        Card[] table = state.getTable();
        for(int i = 0; i < 5; i++) {
            if (table != null && i < table.length) {
                imageViews.get(i).setImage(new Image(path + table[i].toString() + ".png"));
            } else {

                imageViews.get(i).setImage(new Image(path + "back.png"));
            }
        }

        if (state.getHand() != null) {
            player1Card1.setImage(new Image(path + state.getHand().getCard(0).toString() + ".png"));
            player1Card2.setImage(new Image(path + state.getHand().getCard(1).toString() + ".png"));
        }
        else {
            player1Card1.setImage(new Image(path + "back.png"));
            player1Card2.setImage(new Image(path + "back.png"));
        }

        player1NameLabel.setText(state.getMyName());

        if (lastMove != null) {
            player1ActionLabel.setText(lastMove.toString());
        }

        if (state.getOpponentAction() != null) {
            player2NameLabel.setText(state.getOpponentAction().getPlayer());
            player2ActionLabel.setText(state.getOpponentAction().toString());
        }
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
       imageViews = new ArrayList<ImageView>(Arrays.asList(matchCard1, matchCard2, matchCard3, matchCard4, matchCard5));
    }

    private boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }

    @FXML
    public void handleCheckButton(ActionEvent event) {
        if (state != null) {
            lastMove = new PokerMove(state.getMyName(), "check", 0);
            System.out.println(lastMove.toString());
            System.out.flush();
            player1ActionLabel.setText(lastMove.toString());
        }
    }

    @FXML
    public void handleFoldButton(ActionEvent event) {
        if (state != null) {
            lastMove = new PokerMove(state.getMyName(), "fold", 0);
            System.out.println(lastMove.toString());
            System.out.flush();
            player1ActionLabel.setText(lastMove.toString());
        }
    }

    @FXML
    public void handleCallButton(ActionEvent event) {
        if (state != null) {
            lastMove = new PokerMove(state.getMyName(), "call", 0);
            System.out.println(lastMove.toString());
            System.out.flush();
            player1ActionLabel.setText(lastMove.toString());
        }
    }

    @FXML
    public void handleRaiseButton(ActionEvent event) {
        if (state != null) {
            String str = raiseTextfield.getText();
            int value = 0;
            if (isNumeric(str)) {
                value = Integer.parseInt(str);
            }

            lastMove = new PokerMove(state.getMyName(), "raise", value);
            System.out.println(lastMove.toString());
            System.out.flush();
            player1ActionLabel.setText(lastMove.toString());
        }
    }
}
