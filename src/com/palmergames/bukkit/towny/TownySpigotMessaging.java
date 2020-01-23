package com.palmergames.bukkit.towny;

import com.palmergames.bukkit.towny.invites.Invite;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * A class used to encapsulate {@link com.palmergames.bukkit.towny.TownyMessaging}'s
 * spigot-specific code, into a separate container. Contains convenience methods 
 * for sending confirmation and request messages.
 * 
 * @author Suneet Tipirneni (Siris)
 */
public class TownySpigotMessaging {

	/**
	 * Sends an accept/deny clickable message using spigot.
	 * 
	 * @param player Player to send to.
	 * @param invite The invite object to use.
	 */
	public static void sendSpigotRequestMessage(CommandSender player, Invite invite) {
		if (invite.getSender() instanceof Town) { // Town invited Resident
			String firstLine = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Invitation" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE + String.format(TownySettings.getLangString("you_have_been_invited_to_join2"), invite.getSender().getName());
			String secondLine = "/" + TownySettings.getAcceptCommand() + " " + invite.getSender().getName();
			String thirdLine = "/" + TownySettings.getDenyCommand() + " " + invite.getSender().getName();
			sendSpigotConfirmMessage(player, firstLine, secondLine, thirdLine, "");
		}
		if (invite.getSender() instanceof Nation) {
			if (invite.getReceiver() instanceof Town) { // Nation invited Town
				String firstLine = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Invitation" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE + String.format(TownySettings.getLangString("you_have_been_invited_to_join2"), invite.getSender().getName());
				String secondLine = "/t invite accept " + invite.getSender().getName();
				String thirdLine = "/t invite deny " + invite.getSender().getName();
				sendSpigotConfirmMessage(player, firstLine, secondLine, thirdLine, "");
			}
			if (invite.getReceiver() instanceof Nation) { // Nation allied Nation
				String firstLine = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Invitation" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE + String.format(TownySettings.getLangString("you_have_been_requested_to_ally2"), invite.getSender().getName());
				String secondLine = "/n ally accept " + invite.getSender().getName();
				String thirdLine = "/n ally deny " + invite.getSender().getName();
				sendSpigotConfirmMessage(player, firstLine, secondLine, thirdLine, "");
			}
		}
	}

	/**
	 * Sends a player click-able confirmation messages if the server is running on Spigot \(or a fork, like Paper.\)
	 * @param player - The player (CommandSender) to send the confirmation
	 * @param firstline - The question regarding the confirmation.
	 * @param confirmline - Line for sending the confirmation.
	 * @param cancelline - Line for sending the cancellation.
	 * @param lastline - If null, announces that the message will expire. Otherwise, ignored.
	 */
	public static void sendSpigotConfirmMessage(CommandSender player, String firstline, String confirmline, String cancelline, String lastline) {

		if (firstline == null) {
			firstline = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Confirmation" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE + TownySettings.getLangString("are_you_sure_you_want_to_continue");
		}
		if (confirmline == null) {
			confirmline = "/" + TownySettings.getConfirmCommand();
		}
		if (cancelline == null) {
			cancelline = "/" + TownySettings.getCancelCommand();
		}
		if (lastline == null) {
			lastline = ChatColor.BLUE + TownySettings.getLangString("this_message_will_expire");
		} else {
			lastline = "";
		}

		// Create confirm button based on given params.
		TextComponent confirmComponent = new TextComponent(ChatColor.GREEN + confirmline.replace('/', '[').concat("]"));
		confirmComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TownySettings.getLangString("msg_confirmation_spigot_hover_accept")).create()));
		confirmComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, confirmline));

		// Create cancel button based on given params.
		TextComponent cancelComponent = new TextComponent(ChatColor.GREEN + cancelline.replace('/', '[').concat("]"));
		cancelComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TownySettings.getLangString("msg_confirmation_spigot_hover_cancel")).create()));
		cancelComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cancelline));
		
		// Use spigot to send the message.
		player.spigot().sendMessage(new ComponentBuilder(firstline + "\n")
			.append(confirmComponent).append(ChatColor.WHITE + " - " + String.format(TownySettings.getLangString("msg_confirmation_spigot_click_accept"), confirmline.replace('/', '[').replace("[",""), confirmline) + "\n")
			.append(cancelComponent).append(ChatColor.WHITE + " - " + String.format(TownySettings.getLangString("msg_confirmation_spigot_click_cancel"), cancelline.replace('/', '['), cancelline).replace("[","") + "\n")
			.append(lastline)
			.create());
	}
}
