package bot;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.File;

/**
 * Created by kktsang on 30/03/16.
 */
public class FuzzyLogic {

    private FIS fis = null;

    public FuzzyLogic()
    {
        String path = new File("").getAbsolutePath();
        String fileName = path + File.separator + "fuzzy.fcl";
        fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

        FunctionBlock functionBlock = fis.getFunctionBlock("fuzzyBot");

        // Show
//        JFuzzyChart.get().chart(functionBlock);

        // Print ruleSet
//        System.out.println(fis);
    }

    public double evaulateRules(double handEval, double riskEval, double oppBet)
    {
        // Set inputs
        fis.setVariable("handEval", handEval);
        fis.setVariable("riskEval", riskEval);
        fis.setVariable("oppBet", oppBet);

        // Evaluate
        fis.evaluate();

        FunctionBlock functionBlock = fis.getFunctionBlock("fuzzyBot");

        // Show output variable's chart
        Variable playerBet = functionBlock.getVariable("playerBet");
//        JFuzzyChart.get().chart(playerBet, playerBet.getDefuzzifier(), true);

        return playerBet.getValue();
    }

    public static void main (String [] args)
    {
        FuzzyLogic logic = new FuzzyLogic();
        logic.evaulateRules(0.1,0.9,0.9);
    }
}
