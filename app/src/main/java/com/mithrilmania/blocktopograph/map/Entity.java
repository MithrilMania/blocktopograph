package com.mithrilmania.blocktopograph.map;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.mithrilmania.blocktopograph.util.NamedBitmapProvider;
import com.mithrilmania.blocktopograph.util.NamedBitmapProviderHandle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
Entity enum for MCPE -- by @mithrilmania

--- Please attribute @mithrilmania for generating+updating this enum
 */
public enum Entity implements NamedBitmapProviderHandle, NamedBitmapProvider {

    CHICKEN(10, "Chicken", new String[]{ "Chicken", "chicken" }, "chicken", 4),
    COW(11, "Cow", new String[]{ "Cow", "cow" }, "cow", 3),
    PIG(12, "Pig", new String[]{ "Pig", "pig" }, "pig", 1),
    SHEEP(13, "Sheep", new String[]{ "Sheep", "sheep" }, "sheep", 2),
    WOLF(14, "Wolf", new String[]{ "Wolf", "wolf" }, "wolf", 18),
    VILLAGER(15, "Villager", new String[]{ "Villager", "villager", "villager_v2" }, "villager", 7),
    MUSHROOM_COW(16, "Mooshroom", new String[]{ "MushroomCow", "mushroomcow", "mooshroom" }, "mooshroom", 6),
    SQUID(17, "Squid", new String[]{ "Squid", "squid" }, "squid", 5),
    RABBIT(18, "Rabbit", new String[]{ "Rabbit", "rabbit" }, "rabbit", 89),
    BAT(19, "Bat", new String[]{ "Bat", "bat" }, "bat", 64),
    VILLAGER_GOLEM(20, "Iron Golem", new String[]{ "VillagerGolem", "IronGolem", "villagergolem", "irongolem", "iron-golem", "iron_golem" }, "iron-golem", 48),
    SNOW_MAN(21, "Snow Golem", new String[]{ "SnowMan", "SnowGolem", "snowman", "snowgolem", "snow-golem" }, "snow-golem", 31),
    OZELOT(22, "Ocelot", new String[]{ "Ozelot", "Ocelot", "ozelot", "ocelot", "cat" }, "ozelot", 37),
    HORSE(23, "Horse", new String[]{ "EntityHorse", "Horse", "entityhorse", "horse" }, "horse", 73),
    HORSE_DONKEY(24, "Donkey", new String[]{ "EntityHorse", "Donkey", "donkey" }, "donkey", 74),
    HORSE_MULE(25, "Mule", new String[]{ "EntityHorse", "Mule", "mule" }, "mule", 75),
    HORSE_SKELETON(26, "Skeleten Horse", new String[]{ "skeleton_horse", "EntityHorse", "SkeletonHorse", "skeletonhorse", "skeleton-horse" }, "skeleton-horse", 76),
    HORSE_ZOMBIE(27, "Zombie Horse", new String[]{ "EntityHorse", "ZombieHorse", "zombiehorse", "zombie-horse", "zombie_horse" }, "zombie-horse", 77),
    //28
    //29
    ARMOR_STAND(30, "Armor Stand", new String[]{ "ArmorStand", "armorstand", "armor_stand" }, "TODO", 106),
    //31
    ZOMBIE(32, "Zombie", new String[]{ "Zombie", "zombie" }, "zombie", 9),
    CREEPER(33, "Creeper", new String[]{ "Creeper", "creeper" }, "creeper", 14),
    SKELETON(34, "Skeleton", new String[]{ "Skeleton", "skeleton" }, "skeleton", 10),
    SPIDER(35, "Spider", new String[]{ "Spider", "spider" }, "spider", 12),
    PIG_ZOMBIE(36, "Zombie Pigman", new String[]{ "PigZombie", "pigzombie", "zombie-pigman", "zombie_pigman" }, "zombie-pigman", 17),
    SLIME(37, "Slime", new String[]{ "Slime", "slime" }, "slime", 15),
    ENDERMAN(38, "Enderman", new String[]{ "Enderman", "EnderMan", "enderman" }, "enderman", 21),
    SILVERFISH(39, "Silverfish", new String[]{ "Silverfish", "silverfish" }, "silverfish", 22),
    CAVE_SPIDER(40, "Cave Spider", new String[]{ "CaveSpider", "cavespider", "cave-spider", "cave_spider" }, "cave-spider", 118),
    GHAST(41, "Ghast", new String[]{ "Ghast", "ghast" }, "ghast", 16),
    LAVA_SLIME(42, "Magma Cube", new String[]{ "LavaSlime", "lavaslime", "magma-cube", "magma_cube" }, "magma-cube", 24),
    BLAZE(43, "Blaze", new String[]{ "Blaze", "blaze" }, "blaze", 32),
    ZOMBIE_VILLAGER(44, "Zombie Villager", new String[]{ "ZombieVillager", "zombievillager", "zombie_villager_v2", "zombie_villager" }, "zombie-villager", 62),
    WITCH(45, "Witch", new String[]{ "Witch", "witch" }, "witch", 54),
    SKELETON_STRAY(46, "Stray", new String[]{ "Skeleton", "StraySkeleton", "strayskeleton", "stray"}, "stray", 98),
    ZOMBIE_HUSK(47, "Husk", new String[]{ "Zombie", "HuskZombie", "husk", "huskzombie" }, "husk", 99),
    SKELETON_WITHER(48, "Wither Skeleton", new String[]{ "Skeleton", "WitherSkeleton", "wither", "wither_skeleton" }, "wither", 61),
    GUARDIAN(49, "Guardian", new String[]{ "Guardian", "guardian" }, "guardian", 87),
    ELDER_GAURDIAN(50, "Elder Gaurdian", new String[]{ "ElderGaurdian", "elder-guardian", "elderguardian" }, "elder-gaurdian", 88),
    NPC(51, "NPC", new String[]{ "Npc", "npc" }, "npc", 100),
    WITHER_BOSS(52, "Wither Boss", new String[]{ "WitherBoss", "witherboss", "blue-wither-skull" }, "blue-wither-skull", 72),
    ENDER_DRAGON(53, "Ender Dragon", new String[]{ "EnderDragon", "ender-dragon", "enderdragon" }, "ender-dragon", 29),
    SHULKER(54, "Shulker", new String[]{ "Shulker", "shukler", "shulker" }, "shulker", 30),
    ENDERMITE(55, "Endermite", new String[]{ "Endermite", "endermite" }, "endermite", 86),
    LEARN_TO_CODE_MASCOT(56, "Learn To Code Mascot", new String[]{ "LearnToCodeMascot", "learntocodemascot", "learn-to-code-mascot" }, "learn-to-code-mascot", 108),
    GIANT(57, "Giant Zombie", new String[]{ "Giant", "giant" }, "giant", 9),//53 ; Giant is not yet in the game
    //58
    //59
    //60
    //61
    //61
    CAMERA(62, "Tripod Camera", new String[]{ "TripodCamera", "camera" }, "camera", 144),
    PLAYER(63, "Player", new String[]{ "Player", "player" }, "player", 8),
    ITEM(64, "Dropped Item", new String[]{ "ItemEntity", "item" }, "item", -1),//do not render items
    PRIMED_TNT(65, "Primed TNT", new String[]{ "PrimedTnt", "primedtnt", "primed-tnt" }, "primed-tnt", 49),
    FALLING_SAND(66, "Falling Block", new String[]{ "FallingBlock", "falling-sand", "fallingblock" }, "falling-sand", 50),
    ITEM_FRAME(67, "Item Frame", new String[]{ "ItemFrame", "itemframe", "empty-item-frame" }, "empty-item-frame", 66),//67 ; ItemFrame is not yet in the game
    THROWN_EXP_BOTTLE(68, "Bottle o' Enchanting", new String[]{ "ThrownExpBottle", "ExperiencePotion", "thrownexpbottle" }, "ThrownExpBottle", 56),
    XP_ORB(69, "Experience Orb", new String[]{ "XPOrb", "ExperienceOrb", "experience-orb", "experienceorb", "xp_orb" }, "experience-orb", 59),
    EYE_OF_ENDER_SIGNAL(70, "Eye of Ender", new String[]{ "EyeOfEnderSignal", "eyeofendersignal", "eye-of-ender" }, "eye-of-ender", 47),
    ENDER_CRYSTAL(71, "Ender Crystal", new String[]{ "EnderCrystal", "endercrystal", "ender-crystal" }, "ender-crystal", 52),
    //72
    //73
    //74
    //75
    SHULKER_BULLET(76, "Shulker Bullet", new String[]{ "ShulkerBullet", "shulkerbullet", "shulker-bullet" }, "shulker-bullet", 79),
    FISHING_HOOK(77, "Fishing Hook", new String[]{ "FishingHook", "fishinghook", "fishing-hook" }, "fishing-hook", 57),
    CHALKBOARD(78, "Chalkboard", new String[]{ "Chalkboard", "chalkboard" }, "chalkboard", 144),
    DRAGON_FIREBALL(79, "Dragon Fireball", new String[]{ "DragonFireball", "dragonfireball", "dragon-fireball" }, "dragon-fireball", 80),
    ARROW(80, "Arrow", new String[]{ "Arrow", "arrow" }, "arrow", 41),
    SNOWBALL(81, "Snowball", new String[]{ "Snowball", "snowball" }, "snowball", 42),
    THROWN_EGG(82, "Thrown Egg", new String[]{ "ThrownEgg", "thrownegg", "thrown-egg" }, "thrown-egg", 43),
    PAINTING(83, "Painting", new String[]{ "Painting", "painting" }, "painting", 65),
    MINECART_RIDEABLE(84, "Minecart", new String[]{ "MinecartRideable", "Minecart", "minecart" }, "minecart", 34),
    LARGE_FIREBALL(85, "Ghast Fireball", new String[]{ "Fireball", "LargeFireball", "fireball", "largefireball" }, "fireball", 44),
    THROWN_POTION(86, "Splash Potion", new String[]{ "ThrownPotion", "thrownpotion" }, "ThrownPotion", 95),
    THROWN_ENDERPEARL(87, "Ender Pearl", new String[]{ "ThrownEnderpearl", "thrownenderpearl", "ender-pearl" }, "ender-pearl", 46),
    LEASH_KNOT(88, "Lead Knot", new String[]{ "LeashKnot", "LeashFenceKnotEntity", "lead-knot", "leashknot", "leashfenceknotentity", "leash_knot" }, "lead-knot", 94),
    WITHER_SKULL(89, "Wither Skull", new String[]{ "WitherSkull", "wither-skull", "witherskull" }, "wither-skull", 60),
    BOAT(90, "Boat", new String[]{ "Boat", "boat" }, "boat", 33),
    //91
    //92
    LIGHTNING(93, "Lightning Bolt", new String[]{ "LightningBolt", "lightning", "lightningbolt" }, "lightning", 58),
    SMALL_FIREBALL(94, "Blaze Fireball", new String[]{ "SmallFireball", "smallfireball" }, "fireball", 44),
    AREA_EFFECT_CLOUD(95, "Area effect cloud", new String[]{ "AreaEffectCloud", "area-effect-cloud", "areaeffectcloud" }, "area-effect-cloud", 144),
    MINECART_HOPPER(96, "Minecart with Hopper", new String[]{ "MinecartHopper", "minecart-with-hopper", "minecarthopper", "hopper_minecart" }, "minecart-with-hopper", 70),
    MINECART_TNT(97, "Minecart with TNT", new String[]{ "MinecartTNT", "minecart-with-tnt", "minecarttnt", "tnt_minecart" }, "minecart-with-tnt", 69),
    MINECART_CHEST(98, "Storage Minecart", new String[]{ "MinecartChest", "minecart-chest", "minecartchest", "chest_minecart" }, "minecart-chest", 35),
    LINGERING_POTION(101, "Lingering potion", new String[]{ "LingeringPotion", "lingeringpotion", "lingering-potion" }, "lingering-potion", 144),

    CAT(122, "Cat", new String[]{ "Cat" }, "cat", 144),//95 ; FireworksRocketEntity is not in the game yet
    PANDA(123, "Panda", new String[]{ "Panda" }, "panda", 144),//95 ; FireworksRocketEntity is not in the game yet

    //id 900+ is ignored for functions like map-filtering, these are placeholders for when the game adds more expected features.
    MINECART_SPAWNER(900, "Minecart with Spawner", new String[]{ "MinecartSpawner", "minecart-with-spawner", "minecartspawner" }, "minecart-with-spawner", 71),//99 ; MinecartSpawner is not yet in the game
    MINECART_COMMAND_BLOCK(901, "Minecart with Command Block", new String[]{ "MinecartCommandBlock", "minecartcommandblock", "minecart-with-command-block" }, "minecart-with-command-block", 78),//100 ; MinecartCommandBlock is not yet in the game
    MINECART_FURNACE(902, "Powered Minecart", new String[]{ "MinecartFurnace", "minecartfurnace", "minecart-furnace" }, "minecart-furnace", 36),
    FIREWORKS_ROCKET_ENTITY(903, "Firework Rocket", new String[]{ "FireworksRocketEntity", "fireworksrocketentity", "fireworks-rocket" }, "fireworks-rocket", 144),//95 ; FireworksRocketEntity is not in the game yet
    PILLAGER(102, "Pillager", new String[] {"pillager"}, "pillager", 102),
    DROWNED(103, "Drowned", new String[]{ "drowned" }, "drowned", 133),
    PUFFERFISH(104, "Pufferfish", new String[]{ "pufferfish" }, "pufferfish", 129),

    LLAMA(105, "Llama", new String[]{ "llama" }, "llama", 101),
    TROPICALFISH(106, "TropicalFish", new String[]{ "tropicalfish" }, "tropicalfish", 130),
    PARROT(107, "Parrot", new String[]{ "parrot" }, "parrot", 109),
    COD(108, "Cod", new String[]{ "cod" }, "cod", 126),
    SALMON(109, "Salmon", new String[]{ "salmon" }, "salmon", 128),
    TURTLE(110, "Turtle", new String[]{ "turtle" }, "turtle", 123),
    DOLPHIN(111, "Dolphin", new String[]{ "dolphin" }, "dolphin", 135),
    WANDERINGTRADER(112, "WanderingTrader", new String[]{ "wandering_trader" }, "wanderingtrader", 281),
    PHANTOM(113, "Phantom", new String[]{ "phantom" }, "phantom", 125),

    THROWNTRIDENT(114, "ThrownTrident", new String[]{ "thrown_trident" }, "throwntrident", 124),
    UNKNOWN(999, "Unknown", new String[]{ "Unknown", "unknown" }, "unknown", 144);


    public final int id, sheetPos;
    public final String displayName, wikiName;
    public final String[] dataNames;
    public final String identifier;

    public Bitmap bitmap;

    Entity(int id, String displayName, String[] dataNames, String wikiName, int sheetPos){
        this.id = id;
        this.displayName = displayName;
        this.dataNames = dataNames;
        this.wikiName = wikiName;
        this.sheetPos = sheetPos;
        this.identifier = "minecraft:" + wikiName;
    }

    @Override
    public Bitmap getBitmap(){
        return this.bitmap;
    }

    @NonNull
    @Override
    public NamedBitmapProvider getNamedBitmapProvider(){
        return this;
    }

    @NonNull
    @Override
    public String getBitmapDisplayName(){
        return this.displayName;
    }

    @NonNull
    @Override
    public String getBitmapDataName() {
        //First is for actual use, other dataNames are used for retrieval only.
        //This way, one could use the alternative name for manual input, without errors.
        return this.dataNames[0];
    }

    private static final Map<String, Entity> entityMap;
    private static final Map<Integer, Entity> entityByID;

    static {
        entityMap = new HashMap<>();
        entityByID = new HashMap<>();
        for(Entity e : Entity.values()){
            for(String dataName : e.dataNames){
                entityMap.put(dataName, e);
            }
            entityByID.put(e.id, e);
        }
    }

    public static Entity getEntity(String dataName){
        try {
            return entityMap.get(dataName);
        }
        catch (Exception e)
        {
            android.util.Log.d("myTag","error finding id for: " + dataName);
            return entityMap.get(dataName);
        }
    }

    public static Entity getEntity(int id){
        try {
            return entityByID.get(id);
        }
        catch (Exception e )
        {
            android.util.Log.d("myTag","error finding id for: " + id);
            return entityByID.get(id);
        }
    }



    public static void loadEntityBitmaps(AssetManager assetManager) throws IOException {
        Bitmap sheet = BitmapFactory.decodeStream(assetManager.open("entity_wiki.png"));
        int w = sheet.getWidth();
        int tileSize = 16;
        for(Entity e : Entity.values()){
            if(e.bitmap == null && e.sheetPos >= 0){
                //sheetpos; first sprite has pos 1.
                int p = (e.sheetPos-1) * tileSize;
                int x = p % w;
                int y = ((p - x) / w) * tileSize;
                //read tile from sheet, scale to 32x32
                e.bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(sheet, x, y, tileSize, tileSize, null, false), 32, 32, false);
            }
        }
    }
}
