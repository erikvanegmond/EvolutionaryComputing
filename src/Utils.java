/**
 * Created by erikv on 22-10-2016.
 */
public final class Utils {

    public static Individual[] mergeIndividualLists(Individual[] a, Individual[]b){
        Individual[] combined = new Individual[a.length + b.length];
        System.arraycopy(a, 0, combined, 0, a.length);
        System.arraycopy(b, 0, combined, a.length, b.length);
        return combined;
    }
}
