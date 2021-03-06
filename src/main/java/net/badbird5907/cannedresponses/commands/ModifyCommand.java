package net.badbird5907.cannedresponses.commands;

import net.badbird5907.cannedresponses.CannedResponses;
import net.badbird5907.cannedresponses.object.CannedMessage;
import net.badbird5907.jdacommand.annotation.Command;
import net.badbird5907.jdacommand.annotation.Required;
import net.badbird5907.jdacommand.context.CommandContext;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class ModifyCommand {
    @Command(name = "addkeycanned", description = "Add a key to a canned message")
    public void addKey(CommandContext ctx, @Required String cannedMessage, @Required String key) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(cannedMessage);
        if (canned == null) {
            ctx.reply("Could not find that canned message!");
            return;
        }
        if (CannedResponses.getInstance().getConfigManager().getCannedMessage(key) != null) {
            ctx.reply("That key is already in use!");
            return;
        }
        canned.getKeys().add(key.toLowerCase());
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Added key `" + key + "` to " + canned.getName());
    }

    @Command(name = "removekeycanned", description = "Remove a key from a canned message")
    public void delKey(CommandContext ctx, @Required String key) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        canned.getKeys().remove(key.toLowerCase());
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Removed key `" + key + "` from " + canned.getName());
    }

    @Command(name = "setcannedresponse", description = "Set the response for a canned message")
    public void setResponse(CommandContext ctx, @Required String key, @Required String response) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        canned.setResponse(response);
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Set response for " + canned.getName() + " to `" + response + "`");
    }

    @Command(name = "setcannedname", description = "Set the name for a canned message")
    public void setName(CommandContext ctx, @Required String key, @Required String name) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        canned.setName(name);
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Set name for " + canned.getName() + " to `" + name + "`");
    }

    @Command(name = "addkeyword", description = "Add a keyword to a canned message")
    public void addKeyword(CommandContext ctx, @Required String key, @Required String keyword, @Required boolean required) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        if (canned.getKeywords().contains(keyword)) {
            ctx.reply("That keyword is already in use!");
            return;
        }
        canned.setAutomated(true);
        if (required) canned.getKeywordsRequired().add(keyword);
        else canned.getKeywords().add(keyword);
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Added keyword `" + keyword + "` to " + canned.getName() + " (required: " + required + ")");
    }

    @Command(name = "removekeyword", description = "Remove a keyword from a canned message")
    public void removeKeyword(CommandContext ctx, @Required String key, @Required String keyword) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        boolean required, a, b;
        a = canned.getKeywords().remove(keyword);
        b = required = canned.getKeywordsRequired().remove(keyword);
        if (!a && !b) {
            ctx.reply("That keyword is not in use!");
            return;
        }
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        if (canned.getKeywords().isEmpty() && canned.getKeywordsRequired().isEmpty()) {
            canned.setAutomated(false);
        }
        ctx.reply("Removed keyword `" + keyword + "` from " + canned.getName() + " (required: " + required + ")");
    }

    @Command(name = "createcanned", description = "Create a new canned message")
    public void create(CommandContext ctx, @Required String name, @Required String response, @Required String key) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        if (CannedResponses.getInstance().getConfigManager().getCannedMessage(key) != null) {
            ctx.reply("That key is already in use!");
            return;
        }
        CannedMessage canned = new CannedMessage();
        canned.setName(name);
        canned.setResponse(response);
        canned.getKeys().add(key);
        CannedResponses.getInstance().getConfigManager().getMessageConfig().getCannedMessages().add(canned);
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Created new canned message `" + name + "` with key `" + key + "`");
    }

    @Command(name = "setminimum", description = "Set the minimum number of keywords required to trigger a canned message")
    public void setMinimum(CommandContext ctx, @Required String key, @Required int minimum) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        if (minimum < 0) {
            ctx.reply("Minimum must be greater than or equal to 0!");
        } else {
            canned.setMinimumWords(minimum);
            CannedResponses.getInstance().getConfigManager().saveCannedMessages();
            ctx.reply("Set minimum for " + canned.getName() + " to `" + minimum + "`");
        }
    }
    @Command(name = "deletecanned", description = "Delete a canned message")
    public void deleteCanned(CommandContext ctx, @Required String key) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        CannedResponses.getInstance().getConfigManager().getMessageConfig().getCannedMessages().remove(canned);
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Deleted canned message `" + canned.getName() + "`");
    }
    @Command(name = "addignored",description = "Add a ignored channel")
    public void addIgnored(CommandContext ctx, @Required TextChannel channel, @Required String key) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        if (canned.getIgnoredChannels().contains(channel.getIdLong())) {
            ctx.reply("That channel is already ignored!");
            return;
        }
        canned.getIgnoredChannels().add(channel.getIdLong());
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Added channel `" + channel.getName() + "` to ignored channels for `" + canned.getName() + "`");
    }
    @Command(name = "removeignored",description = "Remove a ignored channel")
    public void removeIgnored(CommandContext ctx, @Required TextChannel channel, @Required String key) {
        if (!canModify(ctx)) {
            ctx.reply("Imagine not having permissions L");
            return;
        }
        CannedMessage canned = CannedResponses.getInstance().getConfigManager().getCannedMessage(key);
        if (canned == null) {
            ctx.reply("Could not find that key!");
            return;
        }
        if (!canned.getIgnoredChannels().contains(channel.getIdLong())) {
            ctx.reply("That channel is not ignored!");
            return;
        }
        canned.getIgnoredChannels().remove(channel.getIdLong());
        CannedResponses.getInstance().getConfigManager().saveCannedMessages();
        ctx.reply("Removed channel `" + channel.getName() + "` from ignored channels for `" + canned.getName() + "`");
    }
    public static boolean canModify(CommandContext ctx) {
        return ctx.getMember().getRoles().stream().anyMatch(r -> CannedResponses.getInstance().getConfigManager().getManagerRoles().contains(r.getIdLong()));
    }
}
