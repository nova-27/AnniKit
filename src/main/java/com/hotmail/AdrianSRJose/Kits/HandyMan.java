package com.hotmail.AdrianSRJose.Kits;

 import com.hotmail.AdrianSRJose.AnniPro.anniEvents.NexusHitEvent;
 import com.hotmail.AdrianSRJose.AnniPro.anniGame.AnniPlayer;
 import com.hotmail.AdrianSRJose.AnniPro.anniGame.Game;
 import com.hotmail.AdrianSRJose.AnniPro.kits.Loadout;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;

 import com.hotmail.AdrianSRJose.base.ConfigurableKit;
 import org.bukkit.*;
 import org.bukkit.configuration.ConfigurationSection;
 import org.bukkit.enchantments.Enchantment;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;

 import static com.hotmail.AdrianSRJose.AnniPro.main.AnnihilationMain.API;

public class HandyMan extends ConfigurableKit {

   private final String kit_name = "HandyMan";
   private final Material kit_icon = Material.ANVIL;
   private final List<String> kit_description = new ArrayList<>(Arrays.asList(
           aqua + "HandyMan 直訳すると 便利屋",
           aqua + "え？何かわからない？",
           aqua + "まぁ、The Fixer 修理屋って言ったほうが近いかな？",
           "",
           aqua + "敵軍ネクサスにダメージを与えた際、一定確率で自軍ネクサスが回復するぞ",
           aqua + "確率は、Phase2で35%、Phase3で25%、Phase4で15%、Phase5で10%だぞ",
           aqua + "早めに凸ったほうが有利だな！",
           aqua + "ちなみにだが、体力が開始時の75でも足されていくぜ",
           aqua + "99までだがな（理由は聞くなよ）",
           aqua + "もちろんだが、敗北したチームは復活できない",
           aqua + "残念だったな！"
   ));

   /**
    * configにデフォルト値を設定
    * @param configurationSection configに関する何か
    * @return 0 不明
    */
   @Override
   protected int setDefaults(ConfigurationSection configurationSection) {
     return 0;
   }

   /**
    * configからデータを読み出す
    * @param section configに関する何か
    */
   protected void loadFromConfig(ConfigurationSection section) {}

   /**
    * 初期設定
    * 特殊アイテムがあるときは、設定とかに使う？(Scout参照)
    */
   @Override
   protected void setUp() { }

   /**
    * キットの名前
    * @return キット名
    */
   protected String getInternalName() {
     return kit_name;
   }

   /**
    * キットのアイコン
    * @return アイテムマテリアル（？）
    */
   protected ItemStack getDefaultIcon() {
     return new ItemStack(kit_icon);
   }

   /**
    * キットの説明
    * @return 説明
    */
   protected List<String> getDefaultDescription() {
     return kit_description;
   }

   /**
    * ネクサスを破壊したら
    * @param e 独自イベント
    */
   @EventHandler
   public void nexus(NexusHitEvent e) {
     //自分のチームがﾀﾋんでいるか
     if(e.getPlayer().getTeam().isTeamDead()) return;
     //便利屋であるかどうか
     if(!e.getPlayer().getKit().equals(this)) return;

     //確率の設定
     boolean do_myNexus_fix = false;
     int random = 0;
     switch (Game.getGameMap().getCurrentPhase()) {
       case 2:
         //35% 7/20
         random = (int)(Math.random() * 20);
         if(random >= 0 && random <= 6) do_myNexus_fix = true;
         break;
       case 3:
         //25% 1/4
         random = (int)(Math.random() * 4);
         if(random == 0) do_myNexus_fix = true;
         break;
       case 4:
         //15% 3/20
         random = (int)(Math.random() * 20);
         if(random >= 0 && random <= 2) do_myNexus_fix = true;
         break;
       case 5:
         //10% 1/10
         random = (int)(Math.random() * 10);
         if(random == 0) do_myNexus_fix = true;
         break;
     }

     //ダメージの更新
       if(do_myNexus_fix && (e.getPlayer().getTeam().getHealth() < 99)) {
           API.setTeamHealth(e.getPlayer().getTeam(), e.getPlayer().getTeam().getHealth() + 1);
           Location loc = e.getPlayer().getPlayer().getLocation();
           for(AnniPlayer p : e.getPlayer().getTeam().getPlayers()) {
               loc.add(p.getPlayer().getLocation());
           }
           e.getPlayer().getPlayer().playSound(loc, Sound.NOTE_PIANO,2.5F, 2.0F);
           Bukkit.broadcastMessage(white + e.getPlayer().getName() + green + " repaird your nexus with the Handyman Class!");
       }
   }

   /**
    * 初期武器など
    * @return 初期武器などを設定して返す
    */
   protected Loadout getFinalLoadout() {
     return (new Loadout())
             .addWoodSword()
             .addSoulboundEnchantedItem(new ItemStack(Material.WOOD_PICKAXE), Enchantment.DIG_SPEED, 1)
             .addWoodAxe();
   }

   /**
    * アイテムをクリックしたときに発動
    * @param inv インベントリ
    * @param ap プレイヤー
    * @return true
    */
   public boolean onItemClick(Inventory inv, AnniPlayer ap) {
     addLoadoutToInventory(inv);
     return true;
   }

   /**
    * 天に召したあとにデータを初期化？
    * @param paramPlayer
    */
   public void cleanup(Player paramPlayer) { }

   /**
    * リスポーン時に発動
    * @param p プレイヤー
    * @param ap Anniプレイヤー ←違いは？
    */
   protected void onPlayerRespawn(Player p, AnniPlayer ap) {}

 }