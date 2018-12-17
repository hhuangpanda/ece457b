package bot;

import com.google.common.base.Joiner;

/**
 * Created by huangster on 2016-03-30.
 */
public class StateNode {
    private int number;
    private int strength; // higher is better
    private double base_hand_eval;
    private double base_risk_eval;
    private double hand_eval_increment = 0.1;
    private double risk_eval_increment = 0.1;
    private double q_val = 0.5; // actual weight
    private int k_val = 1; // number of times q_val has been reweighed

    public int getNumber() {
        return number;
    }

    public int getK_val() {
        return k_val;
    }

    public void setK_val(int k_val) {
        this.k_val = k_val;
    }

    public void setQ_val(double q_val) {
        this.q_val = q_val;
    }

    public double getQ_val() {
        return q_val;
    }

    public double getBase_risk_eval() {
        return base_risk_eval;
    }

    public double getBase_hand_eval() {
        return base_hand_eval;
    }

    public int getStrength() {
        return strength;
    }

    public StateNode(int number, int strength, double base_hand_eval, double base_risk_eval, double q_val, int k_val)
    {
        this(number, strength, base_hand_eval, base_risk_eval, q_val, k_val, 0.1, 0.1);
    }

    public StateNode(String[] values){
        this(Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[6]),
                Integer.parseInt(values[7]), Double.parseDouble(values[4]), Double.parseDouble(values[5]));
    }

    public StateNode(int number, int strength, double base_hand_eval, double base_risk_eval, double q_val, int k_val, double hand_eval_increment, double risk_eval_increment){
        this.number = number;
        this.strength = strength;
        this.base_hand_eval = base_hand_eval;
        this. base_risk_eval = base_risk_eval;
        this.q_val = q_val;
        this.k_val = k_val;
        this.hand_eval_increment = hand_eval_increment;
        this.risk_eval_increment = risk_eval_increment;
    }

    @Override
    public String toString(){
        Joiner joiner = Joiner.on(',');
        return joiner.join(number, strength, base_hand_eval,base_risk_eval,hand_eval_increment,risk_eval_increment,q_val,k_val);
    }

    public String getReadableString(){
        Joiner joiner = Joiner.on(',');
        return joiner.join("number: " + number, "strength: " + strength,
                "handEval >= " + base_hand_eval,"riskEval >= " + base_risk_eval,
                hand_eval_increment,risk_eval_increment,
                "Q: " + q_val,"K: " + k_val);
    }
}
