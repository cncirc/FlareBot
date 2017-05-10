package stream.flarebot.flarebot.commands.secret;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.commands.Command;
import stream.flarebot.flarebot.commands.CommandType;
import stream.flarebot.flarebot.commands.FlareBotManager;
import stream.flarebot.flarebot.mod.AutoModGuild;
import stream.flarebot.flarebot.objects.GuildWrapper;

public class TestCommand implements Command {

    @Override
    public void onCommand(User sender, GuildWrapper guild, TextChannel channel, Message message, String[] args, Member member) {
        AutoModGuild autoMod = FlareBotManager.getInstance().getAutoModGuild(channel.getGuild().getId());
        sender.openPrivateChannel().complete().sendMessage(FlareBotManager.GSON.toJson(autoMod)).queue();
        sender.openPrivateChannel().complete().sendMessage(FlareBot.GSON.toJson(guild));
    }

    @Override
    public String getCommand() {
        return "test";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return "{%}test";
    }

    @Override
    public CommandType getType() {
        return CommandType.HIDDEN;
    }
}
