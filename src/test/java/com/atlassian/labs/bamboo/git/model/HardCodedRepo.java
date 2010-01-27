package com.atlassian.labs.bamboo.git.model;

/**
 * A hard-coded model of the actual git repository present in bgit-unittest
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
public class HardCodedRepo {
    public static final CommitDescriptor first = new CommitDescriptor(new Sha("84965cc8dfc8af7fca02c78373413aceafc73c2f"), "Comments", "Fri Oct 9 15:37:45 2009 +0200", "Fri Oct 9 15:37:45 2009 +0200" );
    public static final CommitDescriptor second_a55e =  new CommitDescriptor(new Sha("a55e4702a0fdc210eaa17774dddc4890852396a7"), "File3.txt", "Fri Oct 9 15:38:10 2009 +0200", "Fri Oct 9 15:38:10 2009 +0200", first);
    public static final CommitDescriptor COMMIT_3_Feature1 =  new CommitDescriptor(new Sha("b3035918b551a0cbd72a242e5d00442a1bb59dbe"), "FileF1.txt", "Fri Oct 9 15:39:10 2009 +0200", "Fri Oct 9 15:39:10 2009 +0200", second_a55e);
    public static final CommitDescriptor COMMIT_4_Feature2 =  new CommitDescriptor(new Sha("e352af2f992d9c5f064b24ac7c0af87b4f7c959f"), "FileFeature2.txt", "Fri Oct 9 15:41:28 2009 +0200", "Fri Oct 9 15:41:28 2009 +0200", second_a55e);
    public static final CommitDescriptor COMMIT_2d9b =  new CommitDescriptor(new Sha("2d9b1997d64fa9501a0e4dec26cc9a07e3e8247f"), "OnDefault.txt", "Fri Oct 9 15:42:09 2009 +0200", "Fri Oct 9 15:42:09 2009 +0200", second_a55e);
    public static final CommitDescriptor COMMIT_3a45 =  new CommitDescriptor(new Sha("3a450411d6868221ae290bc0c17695de2990d5d8"), "File4.txt", "Fri Oct 30 11:32:51 2009 +0000", "Fri Oct 30 11:33:53 2009 +0000", COMMIT_2d9b);
    public static final CommitDescriptor COMMIT_fb65 =  new CommitDescriptor(new Sha("fb6562c90de470294b879655a14640ab454ff2ae"), "File5.txt", "Fri Oct 30 11:31:19 2009 +0000", "Fri Oct 30 11:33:53 2009 +0000",  COMMIT_3a45);
    public static final CommitDescriptor COMMIT_5f10 =  new CommitDescriptor(new Sha("5f100f52d0fb2997e50526b4b68d425c69836bdd"), "MergeBranchFile.txt", "Thu Nov 12 21:27:23 2009 +0100", "Thu Nov 12 21:27:23 2009 +0100", second_a55e);
    public static final CommitDescriptor COMMIT_2813 =  new CommitDescriptor(new Sha("281338e6d30d98808e23dc71af487d7b6c3fefc8"), "MergeBranchFile2.txt", "Thu Nov 12 21:28:43 2009 +0100", "Thu Nov 12 21:28:43 2009 +0100", COMMIT_5f10);
    public static final CommitDescriptor COMMIT_7594_Intertwined =  new CommitDescriptor(new Sha("7594c745d20989b4dbdada6a0c1f8a27d8e3660a"), "AIntertwinedFiledDateWise.txt", "Thu Nov 12 21:29:13 2009 +0100", "Thu Nov 12 21:29:13 2009 +0100", COMMIT_fb65);
    public static final CommitDescriptor COMMIT_4208 =  new CommitDescriptor(new Sha("4208d196d885a8a09bc346999aa7a087ff150fb0"), "MergeBranchFile3.txt", "Thu Nov 12 21:29:46 2009 +0100", "Thu Nov 12 21:29:46 2009 +0100", COMMIT_2813);
    public static final CommitDescriptor COMMIT_Merge_aBranch_featureDefault =  new CommitDescriptor(new Sha("34a1c949e2d7e9f138f0fab2c0829c2a134c21a3"), null, "Thu Nov 12 21:30:06 2009 +0100", "Thu Nov 12 21:30:06 2009 +0100", COMMIT_7594_Intertwined, COMMIT_4208);

    public static final CommitDescriptor NONEXISTANT_SHA1 =  new CommitDescriptor(new Sha("046ebe812ad72a76fc35a4f4fb1eb104a4560e55"), "MergeBranchFile3.txt", "Thu Nov 12 21:29:46 2009 +0100", "Thu Nov 12 21:29:46 2009 +0100", COMMIT_2813);

    public static CommitDescriptor getRepoTip(){
        return COMMIT_Merge_aBranch_featureDefault;
    }

    public static CommitDescriptor getBranchPointerFeatureDefault(){
        return COMMIT_Merge_aBranch_featureDefault;
    }


    public static CommitDescriptor getFeature1Head(){
        return COMMIT_3_Feature1;
    }
    public static CommitDescriptor getFeature2Head(){
        return COMMIT_4_Feature2;
    }

    public static CommitDescriptor getFristCommitInBranch(){
            return COMMIT_5f10;
        }

    public static CommitDescriptor getRootCommit(){
            return first;
        }
}
