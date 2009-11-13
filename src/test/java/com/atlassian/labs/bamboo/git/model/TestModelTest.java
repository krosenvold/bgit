package com.atlassian.labs.bamboo.git.model;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * A unit test of the test model. If this fails, the GitRepositoryTest will bork too.
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
public class TestModelTest {

    @Test
    public void testCollectorSimpleLinar(){
        final CommitDescriptor commitDescriptor = HardCodedRepo.getRepoTip();
        List<CommitDescriptor> result = commitDescriptor.collectNodes( HardCodedRepo.COMMIT_2813.getSha());
        assertEquals(2, result.size());
    }

    @Test
    public void testCollectorTwoBranch(){
        final CommitDescriptor commitDescriptor = HardCodedRepo.getRepoTip();
        List<CommitDescriptor> result = commitDescriptor.collectNodes( HardCodedRepo.second_a55e.getSha());
        assertEquals(8, result.size());
    }

    @Test
    public void testTimeOrder(){
        final CommitDescriptor commitDescriptor = HardCodedRepo.getRepoTip();
        CommitList result = commitDescriptor.collectNodesInRealGitLogOrder( HardCodedRepo.second_a55e.getSha());
        Iterator<CommitDescriptor> iter = result.iterator();
        assertEquals( HardCodedRepo.COMMIT_Merge_aBranch_featureDefault, iter.next());
        assertEquals( HardCodedRepo.COMMIT_4208, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_7594_Intertwined, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_2813, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_5f10, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_fb65, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_3a45, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_2d9b, iter.next() );
        assertFalse( iter.hasNext());
    }

    @Test
    public void testSkewedTimeOrder(){
        final CommitDescriptor commitDescriptor = HardCodedRepo.getRepoTip();
        CommitList result = commitDescriptor.collectNodesInRealGitLogOrder( HardCodedRepo.COMMIT_3a45.getSha());
        Iterator<CommitDescriptor> iter = result.iterator();
        assertEquals( HardCodedRepo.COMMIT_Merge_aBranch_featureDefault, iter.next());
        assertEquals( HardCodedRepo.COMMIT_4208, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_7594_Intertwined, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_2813, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_5f10, iter.next() );
        assertEquals( HardCodedRepo.COMMIT_fb65, iter.next() );
        assertFalse( iter.hasNext());
    }

}

