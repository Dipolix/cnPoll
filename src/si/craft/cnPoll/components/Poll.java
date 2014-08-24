package si.craft.cnPoll.components;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import si.craft.cnPoll.main;
import si.craft.cnPoll.helpers.Helper;

public class Poll {

	private static boolean PollActive = false;
	private static ArrayList<String> Voters = new ArrayList<String>();
	private static int yes;
	private static int no;
	private static boolean RecentTimePoll = false;
	private static int RecentTimeTimer = 0;
	private static int TimeTaskID = 0;
	private static boolean RecentWeatherPoll = false;
	private static int RecentWeatherTimer = 0;
	private static int WeatherTaskID = 0;
	public static int PollDelay = 300;

	public static void doPoll(CommandSender sender, Command cmd, String label, String[] args) {
		
		int waitFor;
		final Player player = (Player) sender;
		String playerName = player.getName();
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("create")) {
				
				// construct poll create message
				// get type of poll
				String getPollType = null;
				String pollType;
				if(args[1].equalsIgnoreCase("DayTime")) {
					pollType = "DAY";
					getPollType = "Day";
				}
				else if(args[1].equalsIgnoreCase("Day")) {
					pollType = "DAY";
					getPollType = "Day";
				}
				else if(args[1].equalsIgnoreCase("NightTime")) {
					pollType = "NIGHT";
					getPollType = "Night";
				}
				else if(args[1].equalsIgnoreCase("Night")) {
					pollType = "NIGHT";
					getPollType = "Night";
				}
				else if(args[1].equalsIgnoreCase("ClearWeather")) {
					pollType = "SUN";
					getPollType = "Sun";
				}
				else if(args[1].equalsIgnoreCase("Sun")) {
					pollType = "SUN";
					getPollType = "Sun";
				}
				else if(args[1].equalsIgnoreCase("Storm")) {
					pollType = "RAIN";
					getPollType = "Rain";
				}
				else if(args[1].equalsIgnoreCase("Rain")) {
					pollType = "RAIN";
					getPollType = "Rain";
				}
				else {
					pollType = "HELP";
				}
				
				String Line1 = main.getPlugin().getMainConfig().getString("Messages.Poll.NewPoll.Line1");
			   Line1 = Line1.replace("%dividertop%", main.getPlugin().getMainConfig().getString("Messages.Dividers.Top"));   
				String Line2 = main.getPlugin().getMainConfig().getString("Messages.Poll.NewPoll.Line2");
				Line2 = Line2.replace("%player%", playerName);
				Line2 = Line2.replace("%type%", main.getPlugin().getMainConfig().getString("Messages.Poll.PollType." + getPollType));
				String Line3 = main.getPlugin().getMainConfig().getString("Messages.Poll.NewPoll.Line3");
				Line3 = Line3.replace("%player%", playerName);
				String Line4 = main.getPlugin().getMainConfig().getString("Messages.Poll.NewPoll.Line4");
				String Line5 = main.getPlugin().getMainConfig().getString("Messages.Poll.NewPoll.Line5");
		       Line5 = Line5.replace("%dividerbottom%", main.getPlugin().getMainConfig().getString("Messages.Dividers.Bottom"));

				// Changing the Time
				if (pollType.equalsIgnoreCase("DAY")) {
					if (RecentTimePoll == false) {
						if (PollActive == false) {
							PollActive = true;
							yes = 0;
							no = 0;
							Voters.clear();
							newPollMss(Line1, Line2, Line3, Line4, Line5);
							Voters.add(sender.getName());
							yes++;
							main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(main.getPlugin(),new Runnable() {
								// Calculate and
								// output results
								public void run() {
									if (yes > no) {
										pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Day.isYes"));
										player.getWorld().setTime(0);
									} 
									else if (yes == no) {
										pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Day.isTie"));
										player.getWorld().setTime(0);
									} 
									else if (no > yes) {
										pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Day.isNo"));
									}
									PollActive = false;
									RecentTimePoll = true;
									TimeTaskID = main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(main.getPlugin(),new BukkitRunnable() {
										@Override
										public void run() {
											if (RecentTimeTimer >= PollDelay) {
												RecentTimeTimer = 0;
												RecentTimePoll = false;
												main.getPlugin().getServer().getScheduler().cancelTask(TimeTaskID);
											}
											RecentTimeTimer++;
										}

									},0L,20L);
								}
							}, 600L);
						} 
						// If a current poll is in progress
						else {
							pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.AlreadyActive"));
						}
					}
					// If there has recently been a poll
					else {
						waitFor = (PollDelay - RecentWeatherTimer);
						pollMssWait(sender, waitFor);
					}
				}
				// Weather Poll
				else if (pollType.equalsIgnoreCase("SUN")) {
					if (!RecentWeatherPoll) {
						if (PollActive == false) {
							PollActive = true;
							yes = 0;
							no = 0;
							Voters.clear();
							newPollMss(Line1, Line2, Line3, Line4, Line5);
							Voters.add(sender.getName());
							yes++;
							main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(main.getPlugin(),new Runnable() {
								public void run() {
									if (yes > no) {
										pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Sun.isYes"));
										player.getWorld().setStorm(false);
									} 
									else if (yes == no) {
										pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Sun.isTie"));
										player.getWorld().setStorm(false);
									} 
									else if (no > yes) {
										pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Sun.isNo"));
									}
									PollActive = false;
									RecentWeatherPoll = true;
									WeatherTaskID = main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(main.getPlugin(),new BukkitRunnable() {
										@Override
										public void run() {
											if (RecentWeatherTimer >= PollDelay) {
												RecentWeatherTimer = 0;
												RecentWeatherPoll = false;
												main.getPlugin().getServer().getScheduler().cancelTask(WeatherTaskID);
											}
											RecentWeatherTimer++;
										}

									},0L,20L);
								}
							}, 600L);
						} 
						else {
							pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.AlreadyActive"));
						}
					}
					// If there has recently been a poll
					else {
						waitFor = (PollDelay - RecentWeatherTimer);
						pollMssWait(sender, waitFor);
					}
				}
				// Poll for night time
				else if (pollType.equalsIgnoreCase("NIGHT")) {
					if (RecentTimePoll == false) {
						if (PollActive == false) {
							PollActive = true;
							yes = 0;
							no = 0;
							Voters.clear();
							newPollMss(Line1, Line2, Line3, Line4, Line5);
							Voters.add(sender.getName());
							yes++;
							main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(main.getPlugin(),new Runnable() {public void run() {			
								if (yes > no) {
									pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Night.isYes"));
									player.getWorld().setTime(14000);
								} 
								else if (yes == no) {
									pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Night.isTie"));
									player.getWorld().setTime(14000);
								} 
								else if (no > yes) {
									pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Night.isNo"));
								}
								PollActive = false;
								RecentTimePoll = true;
								TimeTaskID = main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(main.getPlugin(),new BukkitRunnable() {
									@Override
									public void run() {
										if (RecentTimeTimer >= main
												.getPlugin()
												.getMainConfig()
												.getInt("Poll.Delay")) {
											RecentTimeTimer = 0;
											RecentTimePoll = false;
											main
													.getPlugin()
													.getServer()
													.getScheduler()
													.cancelTask(
															TimeTaskID);
										}
										RecentTimeTimer++;
									}
								},0L,20L);
							}}, 600L);
						} 
						else {
							pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.AlreadyActive"));
						}
					} 
					// If there has recently been a poll
					else {
						waitFor = (PollDelay - RecentWeatherTimer);
						pollMssWait(sender, waitFor);
					}
				} 
				// set weather to rain
				else if (pollType.equalsIgnoreCase("RAIN")) {
					if (RecentWeatherPoll == false) {
						if (PollActive == false) {
							PollActive = true;
							yes = 0;
							no = 0;
							Voters.clear();
							newPollMss(Line1, Line2, Line3, Line4, Line5);
							Voters.add(sender.getName());
							yes++;
							main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(main.getPlugin(),new Runnable() {public void run() {
								if (yes > no) {
									pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Rain.isYes"));
									player.getWorld().setStorm(true);
								} 
								else if (yes == no) {
									pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Rain.isTie"));
									player.getWorld().setStorm(true);
								} 
								else if (no > yes) {
									pollResult(yes, no, main.getPlugin().getMainConfig().getString("Messages.Poll.ResultMessages.Rain.isNo"));
								}
								PollActive = false;
								RecentWeatherPoll = true;
								WeatherTaskID = main.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(main.getPlugin(),new BukkitRunnable() {
									@Override
									public void run() {
										if (RecentWeatherTimer >= PollDelay) {
											RecentWeatherTimer = 0;
											RecentWeatherPoll = false;
											main.getPlugin().getServer().getScheduler().cancelTask(WeatherTaskID);
										}
										RecentWeatherTimer++;
									}
								},0L,20L);
							}}, 600L);
						} 
						else {
							pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.AlreadyActive"));
						}
					} 
					// If there has recently been a poll
					else {
						waitFor = (PollDelay - RecentWeatherTimer);
						pollMssWait(sender, waitFor);
					}

				} 
				else if (pollType.equalsIgnoreCase("HELP")) {
					pollHelpMessage(sender);
				}
				else {
					pollHelpMessage(sender);
				}
			}

		} 
		else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("yes")) {
				if (PollActive == true) {
					if (!Voters.contains(player.getName())) {
						yes++;
						Voters.add(player.getName());
						pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.VoteCats"));
					} else {
						pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.AlreadyVoted"));
					}
				} else {
					pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.NoActivePoll"));
				}
			} 
			else if (args[0].equalsIgnoreCase("no")) {
				if (PollActive == true) {
					if (!Voters.contains(player.getName())) {
						no++;
						Voters.add(player.getName());
						pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.VoteCats"));
					} else {
						pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.AlreadyVoted"));
					}
				} else {
					pollMss(sender, main.getPlugin().getMainConfig().getString("Messages.Poll.Common.NoActivePoll"));
				}
			}

		} 
		else {
			pollHelpMessage(sender);
		}

	}
	
	public static void pollResult(int yes, int no, String pollresult) {
		
		String Line1 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line1");
		Line1 = Line1.replace("%dividertop%", main.getPlugin().getMainConfig().getString("Messages.Dividers.Top"));
		
		String Line2 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line2");
		
		String Line3 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line3");
		Line3 = Line3.replace("%yes%", Integer.toString(yes));
		
		String Line4 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line4");
		Line4 = Line4.replace("%no%", Integer.toString(no));
		
		String Line5 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line5");
		Line5 = Line5.replace("%pollresult%", pollresult);
		
		String Line6 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line6");
		Line6 = Line6.replace("%dividerbottom%", main.getPlugin().getMainConfig().getString("Messages.Dividers.Bottom"));
		
		Bukkit.broadcastMessage(Helper.colors(Line1));
		Bukkit.broadcastMessage(Helper.colors(Line2));
		Bukkit.broadcastMessage(Helper.colors(Line3));
		Bukkit.broadcastMessage(Helper.colors(Line4));
		Bukkit.broadcastMessage(Helper.colors(Line5));
		Bukkit.broadcastMessage(Helper.colors(Line6));
	}
	
	public static void pollMss(CommandSender sender, String message) {
		sender.sendMessage(Helper.colors(main.getPlugin().getMainConfig().getString("Messages.Poll.MsgPrefix") + message));
	}
	
	public static void pollMssWait(CommandSender sender, int delay) {
		
		String message = main.getPlugin().getMainConfig().getString("Messages.Poll.Common.WeatherDelay");
		message = message.replace("%delay%", Integer.toString(delay));
		
		pollMss(sender, message);
	}
	
	public static void newPollMss(String Line1, String Line2, String Line3, String Line4, String Line5) {
		Bukkit.broadcastMessage(Helper.colors(Line1));
		Bukkit.broadcastMessage(Helper.colors(Line2));
		Bukkit.broadcastMessage(Helper.colors(Line3));
		Bukkit.broadcastMessage(Helper.colors(Line4));
		Bukkit.broadcastMessage(Helper.colors(Line5));
	}
	
	public static void pollHelpMessage(CommandSender sender) {
	
		String Line1 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line1");
		Line1 = Line1.replace("%dividertop%", main.getPlugin().getMainConfig().getString("Messages.Dividers.Top"));
		
		String Line3 = main.getPlugin().getMainConfig().getString("Messages.Poll.Results.Line6");
		Line3 = Line3.replace("%dividerbottom%", main.getPlugin().getMainConfig().getString("Messages.Dividers.Bottom"));
		
		sender.sendMessage(Helper.colors(Line1));
		sender.sendMessage(Helper.colors(main.getPlugin().getMainConfig().getString("Messages.Poll.PollHelp")));
		sender.sendMessage(Helper.colors(Line3));
		
	}

}

