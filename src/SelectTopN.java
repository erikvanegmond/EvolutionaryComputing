import java.util.Arrays;

/**
 * Created by celestek on 22-10-2016.
 */

public class SelectTopN implements Selector{
    @Override
    public Individual[] select(int n,Individual[] selectFrom){
        if (n < selectFrom.length) {
            try {
                Arrays.sort(selectFrom);
            }catch (Exception e) {
                System.out.println("Ignoring error!!!!");
            }
            Individual[] selected = Arrays.copyOfRange(selectFrom, 0, n);
            return selected;
        } else {
            return null;
        }
    }
}