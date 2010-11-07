<h1>Bamboo Git Plugin</h1>

This plugin provides Git support to the excellent Atlassian Bamboo continous integration server.

It is compatible with Bamboo 2.2.x and above, including 2.6.3.

It also works on Windows, but requires cygwin git (not msysgit).

<h1>Mailing list</h2>
If you're a user you're advised to sign up for the mailing list at
<a href="http://groups.google.com/group/bamboogitplugin">google groups</a>

We're a reasonably quiet bunch, so don't expect your mailbox to explode
just because you sign up. Notifications of issues wrt upgrades or important bugs will be announced on the group.


<h2>Features</h2>

* Full branch support
* Supports fairly avanced git-fu and handles branches, merges and rebases well.
* Regular git usage or git-hub projects.
* Assuming git-hub hooks work with your bamboo version, you should be able to get hooks too.
* Excellent test coverage of all operations

<h2>RELEASES</h2>

The current release is 1.2.2 Only the latest release is available for
<a href="http://cloud.github.com/downloads/krosenvold/bgit/git-plugin-1.2.2.jar">download</a>. Older releases will have to be built from
source, which is really easy.

<h3>Branch/Release policy</h3>

Normally there will be a new binary release every time anything significant changes in
the plugin. Recent activity has mostly been focussed on improving test quality, hence there may be commits
on "master" that are unreleased. "master" always contains the lastest version.

Rebasing *may* happen on non-master branches, master will not be rebased. (I did some rebasing when I took
over maintaining the project)

<h2>HOW TO BUILD FROM SOURCE</h2>

<pre>
git clone git://github.com/krosenvold/bgit.git
cd bgit
# optionally checkout an old version from a tag
mvn install
# the generated .jar will be inside "target" folder.
# If problems with unit tests failing, remove masterRepo and testRepo* folders
# You MUST have git:// protocol access to github.com to be able to build. If not you can add -DskipTests to mvn command.
# If you're running on an old-ish git (for instance 1.6.3.3) an the testCloneThenRebaseLocal test fails, you need to run
# git config --global user.email "you@example.com"
# git config --global user.name "Your Name"
</pre>

A lot of people prefer a double symlink installation of the jar file when building from source:
Assumes bgit is in  ~/bgit
<pre>
ln -s ~/bgit/target/git-plugin-1.2-SNAPSHOT.jar  ~/current-git-plugin
ln -s ~/current-git-plugin $BAMBOO_INSTALL_DIR/webapp/WEB-INF/lib/git-plugin.jar
</pre>
In this way you can just change the symlink in your home directory as the version number changes, instead of
messing with copying the jar file.


<h2>INSTALLATION</h2>
* Copy to $BAMBOO_INSTALL_DIR/webapp/WEB-INF/lib
* Remove older versions from same folder
* Restart Bamboo



Now when you create or edit plans you will be able to select “Git” as the
repository provider.

The plugin delegates most of its configuration to the underlying os shell. As long as this is set up properly, you
should be able to access the repositories. 

All the groundwork was done by Don Brown from Atlassian. Until Atlassian can
provide resources to the plugin I will be mantaining it here on github


<h2>Problem tracking</h2>
It is important that permissions and git setup is correct on ALL nodes (with or without remote agents)

Also ensure that all remote agents AND the local agent can perform the clone, permission problems related to file systems
often give low-quality error messages, often related to complaints about the "checkout" folder.


<h3>==== USAGE WARNING ====</h3>
If you are using SSH make sure to use ssh keys. In general, make sure that a
shell script (running as the same user) can pull from your repo. If it can't then Bamboo will not be
able to checkout neither.

<h2>Release notes</h2>

1.2.2 & 1.2.1

Issue 8, 11 and 12 fixed in these two. Time for a binary release ;) 

1.2 RELEASE NOTES

- Fixed race condition where build agent could check out different version than master bamboo agent thought.
- Fixed issue where the build would not move on when the last-built revision was no longer present in the
  repository due to rebasing. Change detection was returning "no change"
- Does not show full email address - nice to keep those spammers away (Thanks to Luke Taylor)
- Fixed issue with "older" git versions (1.6.3.3) and branch detection.
- Tested agents and agent based builds, and they work well.

1.1.9 RELEASE NOTES

- This version includes greatly improved checkout/fetch/update algorithm that should also handle rebases
  fairly well. Change detection upon rebase is still somewhat in the blue - it shouldn't crash but it won't give
  too much valuable information either. That's what you get for messing with history.
   


1.1.8 RELEASE NOTES

Fixed regression introduced with checkout logic in 1.1.6

1.1.7 RELEASE NOTES
Thanks to Benjamin Reed (RangerRick)
Switched to bamboo 2.5 libs, bamboo 2.5 compatible. Still works on 2.4.x.

1.1.6 RELEASE NOTES
All changes thanks to Ivan Sungurov (isungurov)

- Does not re-clone repository when switching branch
- Internal change to use checkout instead of merge upon update. May improve rebasing..
- Updated to 2.4 libs. Probably still runs on older versions.

1.1.5 RELEASE NOTES

- Fixed problem with rebased repos where out-of order dates would cause bamboo
  to loop infinitely (Thanks to Alex Fisher for patch)
- Works on windows

1.1.4 RELEASE NOTES

- Commit SHA1 numbers are included in top-level build log.
- Better error message when non-existing branch is selected for build

1.1.3 RELEASE NOTES
(big thanks to Kristian Rosenvold)
- This should support branches properly. Please note that when changing
  branches on a build, you still need to clean the build using the bamboo
  console. 

- This should also fix the file list in the commit history view.

- Also, in 1.1.1 you needed to specify “checkout” as a subdirectory name under
  configuration\builder. This should not be done for this version of the plugin
  (the “checkout” folder is still used in the file structure internally, but
  the plugin tells bamboo about it so that you dont have to do it on every
  build!)

- The fix also contains working unit tests for some features.


CHANGELOG
- 2009/10/11: 1.1.2 Several bugfixes and new features
- 2009/04/25: 1.1.1 git submodule support (thanks go for Graeme Mathieson)
- 2009/04/24: Started tracking changes here


<h2>CONTRIBUTORS</h2>

- Don Brown (original author)
- i386 (Bamboo 2.2 support)
- Juan Alonso "slnc" (packaging and updates for bamboo 2.1)
- Graeme Mathieson (git submodule support)
- Kristian Rosenvold (several fixes)
- Alex Fisher (Rebasing fix)
- Ivan Sungurov (isungurov)
- Benjamin Reed (RangerRick)
- Luke Taylor (tekul)
- David Matějček (dmatej)

<h2>Related links</h2>
<ul>
<li><a href="http://labs.atlassian.com/browse/BGIT">Atlassian Lab&#8217;s Bamboo Git plugin page</a> (a little bit outdated)</li>
<li><a href="tp://labs.atlassian.com/wiki/display/BGIT/Home">Atlassian Labs plugin page</a></li>
<li><a href="http://jira.atlassian.com/browse/BAM-2875">Git support in Bamboo issue at Atlassian&#8217;s JIRA</a></li>
<li><a href="http://wiki.github.com/andypols/git-bamboo-plugin">HOWTO Git-hub plugin for Bamboo</a> (it&#8217;s a specific plugin for GitHub but some people found it useful when installing bgit).</li>
</ul>


