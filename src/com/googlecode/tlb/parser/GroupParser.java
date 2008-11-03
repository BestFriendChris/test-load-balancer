// $ANTLR 3.1 /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g 2008-11-02 13:50:27

package com.googlecode.tlb.parser;
	
import com.googlecode.tlb.Groups;
import com.googlecode.tlb.Group;
import com.googlecode.tlb.support.twist.Group;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GroupParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "JOB", "WS", "'['", "']'", "','"
    };
    public static final int T__7=7;
    public static final int T__8=8;
    public static final int WS=5;
    public static final int EOF=-1;
    public static final int T__6=6;
    public static final int JOB=4;

    // delegates
    // delegators


        public GroupParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public GroupParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return GroupParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g"; }


    Groups groups = new Groups();
    Group group = new Group();




    // $ANTLR start "groups"
    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:24:1: groups returns [Groups value = new Groups()] : ( group )+ ;
    public final Groups groups() throws RecognitionException {
        Groups value =  new Groups();

        try {
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:24:45: ( ( group )+ )
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:24:47: ( group )+
            {
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:24:47: ( group )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==6) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:24:48: group
            	    {
            	    pushFollow(FOLLOW_group_in_groups37);
            	    group();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            return this.groups;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "groups"


    // $ANTLR start "group"
    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:25:1: group : '[' jobs ']' ;
    public final void group() throws RecognitionException {
        try {
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:25:7: ( '[' jobs ']' )
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:25:9: '[' jobs ']'
            {
            match(input,6,FOLLOW_6_in_group47); 
            pushFollow(FOLLOW_jobs_in_group49);
            jobs();

            state._fsp--;

            match(input,7,FOLLOW_7_in_group51); 
            this.group = new Group();

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "group"


    // $ANTLR start "jobs"
    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:26:1: jobs : ( job )+ ;
    public final void jobs() throws RecognitionException {
        try {
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:26:7: ( ( job )+ )
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:26:9: ( job )+
            {
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:26:9: ( job )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==JOB||LA2_0==8) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:26:9: job
            	    {
            	    pushFollow(FOLLOW_job_in_jobs60);
            	    job();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);

            this.groups.add(group);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "jobs"


    // $ANTLR start "job"
    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:27:1: job : ( JOB | ',' JOB );
    public final void job() throws RecognitionException {
        Token JOB1=null;
        Token JOB2=null;

        try {
            // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:27:5: ( JOB | ',' JOB )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==JOB) ) {
                alt3=1;
            }
            else if ( (LA3_0==8) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:27:7: JOB
                    {
                    JOB1=(Token)match(input,JOB,FOLLOW_JOB_in_job69); 
                    this.group.addJob((JOB1!=null?JOB1.getText():null));

                    }
                    break;
                case 2 :
                    // /Users/twer/Workspace/test-load-balancer/src/com/googlecode/tlb/parser/Group.g:27:44: ',' JOB
                    {
                    match(input,8,FOLLOW_8_in_job74); 
                    JOB2=(Token)match(input,JOB,FOLLOW_JOB_in_job76); 
                    this.group.addJob((JOB2!=null?JOB2.getText():null));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "job"

    // Delegated rules


 

    public static final BitSet FOLLOW_group_in_groups37 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_6_in_group47 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_jobs_in_group49 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_7_in_group51 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_job_in_jobs60 = new BitSet(new long[]{0x0000000000000112L});
    public static final BitSet FOLLOW_JOB_in_job69 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_8_in_job74 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_JOB_in_job76 = new BitSet(new long[]{0x0000000000000002L});

}