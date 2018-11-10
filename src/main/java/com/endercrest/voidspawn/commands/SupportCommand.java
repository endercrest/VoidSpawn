package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.api.SupportAPIRequest;
import com.endercrest.voidspawn.api.SupportAPIResponse;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SupportCommand implements SubCommand {

    private VoidSpawn plugin;

    public SupportCommand(VoidSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(Player p, String[] args){
        if(args.length < 2) {
            p.sendMessage(MessageUtil.format("&cAn email is required. This is used to respond to support requests"));
            return false;
        } else if (args.length < 3) {
            p.sendMessage(MessageUtil.format("&cA message is required"));
            return false;
        }
        String email = args[1];
        StringBuilder message = new StringBuilder();
        for(int i = 2; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        List<File> files = new ArrayList<>();
        files.add(ConfigManager.getInstance().getWorldFile());

        SupportAPIRequest request = new SupportAPIRequest(email, message.toString(), files);
        request.execute(plugin, response -> {
            if(response.isSuccessful()) {
                p.sendMessage(MessageUtil.format("Support request sent. Please give 2-3 days for a response."));
            } else {
                p.sendMessage(MessageUtil.format("&c" + response.getMessage()));
            }
        });

        p.sendMessage(MessageUtil.format("Sending support request..."));
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs support [email] [message] - Sends a support request.";
    }

    @Override
    public String permission(){
        return "vs.admin.support";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args){
        return null;
    }
}
