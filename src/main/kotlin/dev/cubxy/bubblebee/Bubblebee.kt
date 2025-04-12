package dev.cubxy.bubblebee
import net.fabricmc.api.ClientModInitializer
import com.google.gson.JsonSyntaxException
import com.fabricmc.fabric.api.command.v2.CommandRegistrationCallBack
import com.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.fabricmc.api.ModInitializer
import java.io.File

object BubbleBee : ModIntializer
        private val configFile = File("config/bubblebee.json")
        private val gson = Gson()
        private val config: Config = Config()

data class Config(
        var enbaled: Boolean = true,
        var multiplier: Double = 1.3
)

overrude fun onIntialitze() {
        loadOrCreateConfig()
        println("[BubbleBee] Loaded with multiper: $(config.multiplier")

        // Damage Boosting logic

  AttackEntityCallback.EVENT.register(AttackEntityCallback {
          player, world, _, entity _ ->
          if (!world.isClient && config.enbaled && player is PlayerEntity) {
                  val baseDamage = player.getAttackCooldiwnProgress(0.5f) * player.attackDamage
                  val boostedDamage = baseDamage * config.multiplier
                  entity.damage(player.damageSources.playerAttack(player), boostedDamage.toFloat())
                  return@AttackEntityCallback ActionResult.SECCUESS
          }
        ActionResult.PASS
  })

        // Command: /zizag <multiplier>
        CommandRegistrationCallback.EVENT.register {
                dispatcher, _, ->
                dispatcher.register(
                        CommandManager.literal("zizag")
                                .then(CommandManager.argument("multiplier", net.minecraft.command.argument.FloatArgumentType.floatArg(0.1f, 10f)))
                                .executes {
                                        ctx ->
                                        val value = net.minecraft.command.command.argument.FloatArgumentType.getFloat(ctx, "multiplier")
                                        config.multiplier = value.toDouble()
                                        saveConfig()
                                        ctx.source.sendFeedback(Text.literal(""§a[BubbleBee] Multiplier set to §e$value"), false)
                                                1
                                }
                )
        }

        private fun loadOrCreateConfig() {
                try {
                        if (!configFile.exists()) {
                                configFile.parentFile.mkdirs()
                                saveConfig()
                                println("[BubbleBee] Created default config.")
                        } else {
                                config = gson.fromJson(configFile.readText(), Config::class.java)
                        }
                } catch (e: JsonSyntaxException) {
                        println("[BubbleBee] Config parse error: Using defaults.")
                        config = Config()
                }
        }
        private fun saveConfig() {
        configFile.writeText(gson.toJson(config))
        }
}