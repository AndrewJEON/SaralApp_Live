package android.support.v4.app;




import java.util.ArrayList;
import java.util.List;

public class SupportV4App {
    public static void activityFragmentsNoteStateNotSaved(FragmentActivity activity) {
        activity.mFragments.noteStateNotSaved();


    }

    public static List<Fragment> activityFragmentsActive(FragmentActivity activity) {
       // activity.mFragments.mActive
        return activity.mFragments.retainNonConfig() ;
    }

    public static int fragmentIndex(Fragment fragment) {
        return fragment.mIndex;
    }

    public static ArrayList<Fragment> fragmentChildFragmentManagerActive(Fragment fragment) {
        return ((FragmentManagerImpl) fragment.getChildFragmentManager()).mActive;
    }
}
