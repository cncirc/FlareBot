package stream.flarebot.flarebot.util;

import stream.flarebot.flarebot.FlareBot;
import stream.flarebot.flarebot.commands.Command;
import stream.flarebot.flarebot.commands.general.TagsCommand;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsageParser {

    public static void test(String... ss) {
        for (String s : matchUsage(new TagsCommand(), ss)) {
            FlareBot.getInstance().getImportantLogChannel().sendMessage(s).queue();
        }
    }

    public static List<String> matchUsage(Command c, String[] args) {
        List<String> strings = new ArrayList<>();
        if (args.length == 0) {
            Collections.addAll(strings, c.getUsage().split("\n"));
            return strings;
        }
        for (String usage : c.getUsage().split("\n")) {
            Pattern p = Pattern.compile("\\`.+\\`");
            String symbols = usage.replace("{%}" + c.getCommand(), "").trim();
            Matcher m = p.matcher(symbols);
            if (m.find()) {
                symbols = m.group().replace("`", "").trim();
            } else {
                continue;
            }
            Map<Integer, Pair<Symbol, String>> map = findSymbols(symbols);
            boolean applicable = false;
            if (args.length > map.size()) {
                continue;
            }
            for (Map.Entry<Integer, Pair<Symbol, String>> entry : map.entrySet()) {
                if (entry.getValue().getKey() == Symbol.SINGLE_SUB_COMMAND) {
                    if (args[entry.getKey()].equalsIgnoreCase(entry.getValue().getValue())) {
                        applicable = true;
                    } else {
                        break;
                    }
                } else if (entry.getValue().getKey() == Symbol.MULTIPLE_SUB_COMMAND) {
                    for (String cmd : entry.getValue().getValue().split("\\|")) {
                        if (args[entry.getKey()].equalsIgnoreCase(cmd)) applicable = true;
                        break;
                    }
                    if (!applicable) break;
                } else {
                    applicable = true;
                }
            }

            if (applicable) strings.add(usage);
        }
        return strings;
    }

    public static Map<Integer, Pair<Symbol, String>> findSymbols(String string) {
        Map<Integer, Pair<Symbol, String>> map = new HashMap<>();
        int i = 0;
        for (String s : string.split(" ")) {
            for (Symbol sy : Symbol.values()) {
                if (sy.matches(s)) map.put(i, new Pair<Symbol, String>(sy, s));
            }
            i++;
        }
        return map;
    }


    public enum Symbol {
        SINGLE_SUB_COMMAND("^[A-Za-z]+$"),
        MULTIPLE_SUB_COMMAND("^[A-z]+(\\|+[A-z]+)+$"),
        REQUIRED_ARG("^<.+>$"),
        OPTIONAL_ARG("^\\[.+\\]$");

        private String regex;

        Symbol(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }

        public boolean matches(String arg) {
            return arg.matches(regex);
        }

    }

}
