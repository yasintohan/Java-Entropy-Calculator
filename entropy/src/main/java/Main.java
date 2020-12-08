import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {
    List<String> nowsterms = new ArrayList<>();
    Map<String, Integer> map = new HashMap<>();
    int topN = 0;

    public Main(String fileName, int topN) throws IOException {
        this.topN = topN;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<String> names = new ArrayList<>();
        String line;
        StringTokenizer myTokens;

        while ((line = br.readLine()) != null) {

            myTokens = new StringTokenizer(line, " ");

            while (myTokens.hasMoreTokens()) {
                names.add(myTokens.nextToken());
            }
        }
        for (int i = 0; i < names.size(); i++) {
            nowsterms.add(names.get(i).replaceAll("\\p{Punct}", "").toLowerCase().trim());
        }

        computeEntropy();
        computeAvgLengthByFirstChar();
        Set pairs = calculateMinPairDist();

    }


    private double computeEntropy() throws FileNotFoundException {


        // count the occurrences of each value
        for (String sequence : nowsterms) {
            if (!map.containsKey(sequence)) {
                map.put(sequence, 0);
            }

            map.put(sequence, map.get(sequence)+1);
        }
        // calculate the entropy
        Double result = 0.0;
        for (String sequence : map.keySet()) {
            Double frequency = (double) map.get(sequence) / nowsterms.size();
            result -= frequency * (Math.log(frequency) / Math.log(2));
        }

        System.out.println("Entropy=" + result);

        return result;

    }


         private void computeAvgLengthByFirstChar() {

             int total[] = new int[256];

             Map<Integer, Integer> maplengt = new HashMap<>();

             //initial character counter
             for (String sequence : nowsterms) {
                 if (!maplengt.containsKey((int) sequence.charAt(0))) {
                     maplengt.put((int) sequence.charAt(0), 0);
                 }

                 maplengt.put((int) sequence.charAt(0), maplengt.get((int) sequence.charAt(0))+1);
             }//#counter


             //total character count
             for (int i=0;i<nowsterms.size();i++) {

                 total[(int) nowsterms.get(i).charAt(0)] += nowsterms.get(i).length();

                 }//#total counter

             //InitialCharacter AverageLength counter
             System.out.println("InitialCharacter AverageLength");
             for (int i=0;i<total.length;i++) {
                 if(total[i] != 0) {
                     System.out.println((char) i + " " + (double)total[i] / (double)maplengt.get(i));
                 }

             }//#InitialCharacter AverageLength counter


         }


        private Set calculateMinPairDist() {
            HashSet<String> set = new HashSet<String>();
            double[][] scores = new double[map.size()*(map.size()-1)][3];


            String key;
            double score;
            int order = 0;
            //terms calculate
            for (Map.Entry term1 : map.entrySet()) {
                for (Map.Entry term2 : map.entrySet()) {
                    if(!term1.getKey().equals(term2.getKey())) {
                       score = ((int)term1.getValue()* (int) term2.getValue())/(1 + Math.log(minDist(term1.getKey().toString(), term2.getKey().toString())));

                        key = "Pair{t1='" + term1.getKey() + "', t2='" + term2.getKey() + "', score="+ score + "}";
                        set.add(key);

                        scores[order][0] = score;
                        scores[order][1] = nowsterms.indexOf(term1.getKey());
                        scores[order][2] = nowsterms.indexOf(term2.getKey());
                        order++;

                    }
                }
            }
            //#terms calculate

            //Array Sort
            Arrays.sort(scores, new Comparator<double[]>() {
                public int compare(double[] a, double[] b) {
                    return Double.compare(b[0], a[0]);
                }
            });
            //#Array Sort

            //print topN
            System.out.println("Top "+ topN +" Minimum Pair Distance");
            for (int i = 0; i<topN; i++) {

                    System.out.println("Pair{t1='" + nowsterms.get((int)scores[i][1]) + "', t2='" + nowsterms.get((int)scores[i][2])+ "', score="+ scores[i][0] + "}");

            }
            //#print topN

            return set;
        }

    public double minDist(String x, String y)
    {
        int totalmin = 0;
        //find terms
        List<Integer> terms = new ArrayList<>();
        for(int i = 0; i<nowsterms.size();i++) {
            if(y.equals(nowsterms.get(i)))
                terms.add(i);
        }
        //#find terms


        //find minimum distance
        for(int i = 0; i<terms.size(); i++) {
            int min_dist = Integer.MAX_VALUE;
            for (int j = 0; j < nowsterms.size(); j++)
            {
                if ((x.equals(nowsterms.get(j)) && !x.equals(nowsterms.get(terms.get(i))))
                        && min_dist > Math.abs(terms.get(i) - j) && !x.equals(y)) {
                    min_dist = Math.abs(terms.get(i) - j);
                    }

            }
            totalmin += min_dist;
        }
        //#find minimum distance
        return totalmin;
    }



            public static void main (String[]args) throws IOException {
                new Main(args[0],Integer.parseInt(args[1]));

            }
}




