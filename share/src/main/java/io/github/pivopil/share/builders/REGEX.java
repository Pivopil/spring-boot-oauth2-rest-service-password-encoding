package io.github.pivopil.share.builders;

/**
 * Created on 18.01.17.
 */
public class REGEX {

    private REGEX(){}

    public static final String SHA1 = "[a-fA-F0-9]{40}";
    public static final String EMAIL = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$";


}
