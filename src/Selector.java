/**
 * Created by erikv on 22-10-2016.
 */
public interface Selector {

    /**
     * Selects n individuals from selectFrom
     */
    Individual[] select(int n, Individual[] selectFrom);
}
