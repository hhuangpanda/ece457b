package bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Created by huangster on 2016-03-30.
 */
public class Reinforcement {
    static final Logger logger = LogManager.getLogger(Reinforcement.class);
    private static int DIMENSION_SIZE = 10;
    StateNode[] states = new StateNode[DIMENSION_SIZE*DIMENSION_SIZE];
    private StateNode previousState = null;
    private boolean contradictAction = false;

    public Reinforcement()
    {
        this(false);
    }

    public Reinforcement(boolean readFile){
        this(readFile, "");
    }

    public Reinforcement(boolean readFile, String name){
        if(readFile && name != null && name.length() > 0){
            //String path = System.getProperty("java.class.path");
            //String filePath = path + File.pathSeparator + name;
            logger.info(String.format("Fetching %s", name));
            int lineNumber = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(name))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if(lineNumber == states.length) break;
                    states[lineNumber] = new StateNode(values);
                    lineNumber++;
                }
            }
            catch(FileNotFoundException ex){
                logger.error(name + " file not found");
                logger.error(ex.getStackTrace());
            }
            catch(IOException ex){
                logger.error(ex.getStackTrace());
            }
        }
        else{
            for(int i = 0; i < DIMENSION_SIZE; ++i){
                double base_hand_eval = i*(1.0/DIMENSION_SIZE);
                for(int j = 0; j < DIMENSION_SIZE; ++j){
                    double base_risk_eval = j*(1.0/DIMENSION_SIZE);
                    int number = i*DIMENSION_SIZE + j;
                    states[number] = new StateNode(number,calculateStrength(base_hand_eval,base_risk_eval),
                            base_hand_eval,base_risk_eval,0.5,1);
                }
            }
        }
    }

    public boolean isContradictAction() {
        return contradictAction;
    }

    public void setContradictAction(boolean contradictAction) {
        this.contradictAction = contradictAction;
    }

    /**
     *
     * @param handEval
     * @param riskEval
     * @return 1 to play, 0 to not play
     */
    public boolean playAction(double handEval, double riskEval)
    {
        int handEvalNumber = (int)(handEval*DIMENSION_SIZE*DIMENSION_SIZE);
        // hand evaluation is tens digit, risk evaluation is singles digit
        int number = (int)(Math.round(handEvalNumber/10.0))*10 + (int)(riskEval*DIMENSION_SIZE);
        // reward if the strength of the current state is better than previous and if we didn't override the command
        if(previousState != null && !contradictAction){
            int k = previousState.getK_val();
            double q = previousState.getQ_val();
            double new_q = q;
            // current strength if better than previous then reward
            if(previousState.getStrength() <= states[number].getStrength()){
                // averaging the rewards in the Q function
                new_q = q + (1.0/k)*(1-q);
            }
            else{
                // averaging the punishment
                new_q = q + (1.0/k)*(0-q);
            }
            previousState.setK_val(k+1);
            previousState.setQ_val(new_q);
        }
        contradictAction = false;
        previousState = states[number];
        // do current action based on hand and risk
        double value = amountToPutIn(handEval, riskEval);
        // higher value means more chances of playing
        if(Math.random() < value){
            return true;
        }
        return false;
    }

    public double amountToPutIn(double handEval, double riskEval){
        return handEval - Math.tanh(8*(riskEval-0.4));  // can be tweaked
    }

    // high risk is worse than good hand
    public int calculateStrength(double handEval, double riskEval){
        return (int)(10*handEval + (1-riskEval)*20);
    }

    public void printNetwork()
    {
        for(int i = 0; i < DIMENSION_SIZE*DIMENSION_SIZE; ++i){
            logger.info(states[i].toString());
        }
    }

    public void recordNetwork(String fileName)
    {
//        String filePath = System.getProperty("java.class.path");
//        if(fileName == null || fileName.length() == 0){
//            filePath = filePath + File.pathSeparator + "reinforcement_" + DIMENSION_SIZE + "_" + DIMENSION_SIZE + ".txt";
//        }
//        else{
//            filePath = filePath + File.pathSeparator + fileName;
//        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "utf-8"))) {
            for(int i = 0; i < states.length; ++i){
                writer.write(states[i].toString());
                writer.newLine();
            }
        }
        catch(IOException ex){
            logger.error(ex.getStackTrace());
        }
    }
}
