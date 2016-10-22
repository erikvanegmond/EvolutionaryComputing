import java.util.Arrays;

/**
 * Created by celestek on 22-10-2016.
 */

public class SelectTopN implements Selector{
    @Override
    public Individual[] select(int n,Individual[] selectFrom){
        try {
            if (n < selectFrom.length) {
                Arrays.sort(selectFrom);
                Individual[] selected = Arrays.copyOfRange(selectFrom, 0, n);
                return selected;
            } else {
                return null;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}