package app.snc;

public class improved_summary {
        String result;

        public improved_summary(){
                result = null;
                SummaryTool summary = new SummaryTool();
                summary.init();
                summary.extractSentenceFromContext();
                summary.groupSentencesIntoParagraphs();
                summary.printSentences();
                summary.createIntersectionMatrix();

                //System.out.println("INTERSECTION MATRIX");
                //summary.printIntersectionMatrix();

                summary.createDictionary();
                //summary.printDicationary();

                System.out.println("SUMMMARY");
                summary.createSummary();
                result = summary.printSummary();

                summary.printStats();
    }

        public String getResult() {
                return result;
        }
}
