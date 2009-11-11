<h1>Bamboo Git Plugin</h1>

This plugin provides Git support to the excellent Atlassian Bamboo continou integration server.

It is compatible with Bamboo 2.2.x and above.

    
<h2>RELEASES</h2>

The current release is 1.1.5. Only the latest release is available for <a href="http://cloud.github.com/downloads/krosenvold/bgit/git-plugin-1.1.5.jar">download</a>. Older releases will have to be built from source, which is really easy:

<h2>HOW TO BUILD FROM SOURCE</h2>

<pre>
git clone git://github.com/krosenvold/bgit.git
cd bgit
# optionally checkout an old version from a tag
mvn install
# the generated .jar will be inside "target" folder.
# If problems with unit tests failing, remove masterRepo and testRepo* folders
# You MUST have git:// protocol access to github.com to be able to build
</pre>

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
==== USAGE WARNING ====
If you are using SSH make sure to use ssh keys. In general, make sure that a
background script can pull from your repo. If it can't then Bamboo will not be
able to checkout neither.

<h2>Release notes</h2>
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

<h2>Related links</h2>
<ul>
<li><a href="http://labs.atlassian.com/browse/BGIT">Atlassian Lab&#8217;s Bamboo Git plugin page</a> (a little bit outdated)</li>
<li><a href="tp://labs.atlassian.com/wiki/display/BGIT/Home">Atlassian Labs plugin page</a></li>
<li><a href="http://jira.atlassian.com/browse/BAM-2875">Git support in Bamboo issue at Atlassian&#8217;s JIRA</a></li>
<li><a href="http://wiki.github.com/andypols/git-bamboo-plugin">HOWTO Git-hub plugin for Bamboo</a> (it&#8217;s a specific plugin for GitHub but some people found it useful when installing bgit).</li>
</ul>


