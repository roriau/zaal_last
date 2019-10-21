package mn.zaal.zaal.tuslah;

import net.yslibrary.simplepreferences.annotation.Key;
import net.yslibrary.simplepreferences.annotation.Preferences;

@Preferences(useDefault = true)
public class Config {

    @Key(omitGetterPrefix = true)
    protected boolean isPermAccepted = false;

    @Key(omitGetterPrefix = true)
    protected boolean isFirstTime = true;

    @Key
    protected int userId = 0;

    @Key
    protected String phoneNumber;

    @Key(omitGetterPrefix = true)
    protected boolean isLoggedin = false;

}
