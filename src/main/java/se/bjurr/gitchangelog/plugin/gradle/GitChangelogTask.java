package se.bjurr.gitchangelog.plugin.gradle;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static se.bjurr.gitchangelog.api.GitChangelogApi.gitChangelogApiBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.bjurr.gitchangelog.api.GitChangelogApi;

public class GitChangelogTask extends DefaultTask {

 private static final Logger log = LoggerFactory.getLogger(DefaultTask.class.getName());
 private String toRef;
 private String toCommit;

 private String fromRef;
 private String fromCommit;

 private String settingsFile;
 private String templateContent;
 private String filePath;

 private String mediaWikiUrl;
 private String mediaWikiTitle;
 private String mediaWikiUsername;
 private String mediaWikiPassword;

 private String readableTagName;
 private String dateFormat;
 private String timeZone;
 private boolean removeIssueFromMessage;
 private String ignoreCommitsIfMessageMatches;
 private String untaggedName;
 private String noIssueName;

 private List<List<String>> customIssues = newArrayList();

 public void setCustomIssues(List<List<String>> customIssues) {
  this.customIssues = customIssues;
 }

 public List<List<String>> getCustomIssues() {
  return customIssues;
 }

 public void setTemplateContent(String templateContent) {
  this.templateContent = templateContent;
 }

 public String getTemplateContent() {
  return templateContent;
 }

 public String getFromCommit() {
  return fromCommit;
 }

 public String getSettingsFile() {
  return settingsFile;
 }

 public String getFilePath() {
  return filePath;
 }

 public String getFromRef() {
  return fromRef;
 }

 public String getToCommit() {
  return toCommit;
 }

 public String getToRef() {
  return toRef;
 }

 public void setFilePath(String filePath) {
  this.filePath = filePath;
 }

 public void setFromCommit(String fromCommit) {
  this.fromCommit = fromCommit;
 }

 public void setFromRef(String fromRef) {
  this.fromRef = fromRef;
 }

 public void setSettingsFile(String settingsFile) {
  this.settingsFile = settingsFile;
 }

 public void setToCommit(String toCommit) {
  this.toCommit = toCommit;
 }

 public void setToRef(String toRef) {
  this.toRef = toRef;
 }

 public void setMediaWikiPassword(String mediaWikiPassword) {
  this.mediaWikiPassword = mediaWikiPassword;
 }

 public void setMediaWikiTitle(String mediaWikiTitle) {
  this.mediaWikiTitle = mediaWikiTitle;
 }

 public void setMediaWikiUrl(String mediaWikiUrl) {
  this.mediaWikiUrl = mediaWikiUrl;
 }

 public void setMediaWikiUsername(String mediaWikiUsername) {
  this.mediaWikiUsername = mediaWikiUsername;
 }

 public String getMediaWikiPassword() {
  return mediaWikiPassword;
 }

 public String getMediaWikiTitle() {
  return mediaWikiTitle;
 }

 public String getMediaWikiUrl() {
  return mediaWikiUrl;
 }

 public String getMediaWikiUsername() {
  return mediaWikiUsername;
 }

 public void setDateFormat(String dateFormat) {
  this.dateFormat = dateFormat;
 }

 public void setIgnoreCommitsIfMessageMatches(String ignoreCommitsIfMessageMatches) {
  this.ignoreCommitsIfMessageMatches = ignoreCommitsIfMessageMatches;
 }

 public void setNoIssueName(String noIssueName) {
  this.noIssueName = noIssueName;
 }

 public void setReadableTagName(String readableTagName) {
  this.readableTagName = readableTagName;
 }

 public void setTimeZone(String timeZone) {
  this.timeZone = timeZone;
 }

 public void setUntaggedName(String untaggedName) {
  this.untaggedName = untaggedName;
 }

 public String getDateFormat() {
  return dateFormat;
 }

 public boolean getRemoveIssueFromMessage() {
  return removeIssueFromMessage;
 }

 public void setRemoveIssueFromMessage(boolean removeIssueFromMessage) {
  this.removeIssueFromMessage = removeIssueFromMessage;
 }

 public String getIgnoreCommitsIfMessageMatches() {
  return ignoreCommitsIfMessageMatches;
 }

 public String getNoIssueName() {
  return noIssueName;
 }

 public String getReadableTagName() {
  return readableTagName;
 }

 public String getTimeZone() {
  return timeZone;
 }

 public String getUntaggedName() {
  return untaggedName;
 }

 @TaskAction
 public void gitChangelogPluginTasks() throws TaskExecutionException {
  try {
   getProject().getExtensions().findByType(GitChangelogPluginExtension.class);

   GitChangelogApi builder;
   builder = gitChangelogApiBuilder();
   if (isSupplied(settingsFile)) {
    builder.withSettings(new File(settingsFile).toURI().toURL());
   }

   if (isSupplied(toRef)) {
    builder.withToRef(toRef);
   }

   if (isSupplied(templateContent)) {
    builder.withTemplateContent(templateContent);
   }
   if (isSupplied(fromCommit)) {
    builder.withFromCommit(fromCommit);
   }
   if (isSupplied(fromRef)) {
    builder.withFromRef(fromRef);
   }
   if (isSupplied(toCommit)) {
    builder.withToCommit(toCommit);
   }

   if (isSupplied(readableTagName)) {
    builder.withReadableTagName(readableTagName);
   }
   if (isSupplied(dateFormat)) {
    builder.withDateFormat(dateFormat);
   }
   if (isSupplied(timeZone)) {
    builder.withTimeZone(timeZone);
   }
   builder.withRemoveIssueFromMessageArgument(removeIssueFromMessage);
   if (isSupplied(ignoreCommitsIfMessageMatches)) {
    builder.withIgnoreCommitsWithMesssage(ignoreCommitsIfMessageMatches);
   }
   if (isSupplied(untaggedName)) {
    builder.withUntaggedName(untaggedName);
   }
   if (isSupplied(noIssueName)) {
    builder.withNoIssueName(noIssueName);
   }
   for (List<String> customIssue : customIssues) {
    String name = customIssue.get(0);
    String pattern = customIssue.get(1);
    String link = null;
    if (customIssue.size() > 2) {
     link = customIssue.get(2);
    }
    builder.withCustomIssue(name, pattern, link);
   }

   if (isSupplied(filePath)) {
    builder.toFile(filePath);
    log.info("#");
    log.info("# Wrote: " + filePath);
    log.info("#");
   }

   if (isSupplied(mediaWikiUrl)) {
    builder//
      .toMediaWiki(//
        mediaWikiUsername,//
        mediaWikiPassword, //
        mediaWikiUrl,//
        mediaWikiTitle);
    log.info("#");
    log.info("# Created: " + mediaWikiUrl + "/index.php/" + mediaWikiTitle);
    log.info("#");
   }
  } catch (MalformedURLException e) {
   log.error("GitChangelog", e);
  }
 }

 private boolean isSupplied(String param) {
  return !isNullOrEmpty(param);
 }
}
