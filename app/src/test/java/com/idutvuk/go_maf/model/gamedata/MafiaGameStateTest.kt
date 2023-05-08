package com.idutvuk.go_maf.model.gamedata


import com.google.common.truth.Truth.assertThat
import com.idutvuk.go_maf.model.generateRoles
import org.junit.Before
import org.junit.Test

class MafiaGameStateTest {
    lateinit var gameState: MafiaGameState
    @Before
    fun generateRandomRoles() {
        val n = 10 //players count
        val roles = generateRoles(n)
        gameState = MafiaGameState(n, players = Array(
            n, init = {
                Player(
                    it,
                    role = roles[it])
            }
        ))
    }

    @Test
    fun `no alive black players returns true`() {
        val gameState = MafiaGameState(10)
        assertThat(gameState.isGameOver()).isTrue()
    }

    @Test
    fun `equal amount of red and black players returns true`() {
        val gameState = MafiaGameState(6, players = arrayOf(
            Player(0,Role.CIV),
            Player(1,Role.CIV),
            Player(2,Role.CIV),
            Player(3,Role.SHR),
            Player(4,Role.DON),
            Player(5,Role.MAF),
            Player(3,Role.MAF),
        ))
        assertThat(gameState.isGameOver()).isTrue()
    }

    @Test
    fun `red more than black returns false`() {
        val gameState = MafiaGameState(6, players = arrayOf(
            Player(0,Role.CIV),
            Player(1,Role.CIV),
            Player(2,Role.CIV),
            Player(3,Role.SHR),
            Player(4,Role.DON),
            Player(5,Role.MAF),
        ))
        assertThat(gameState.isGameOver()).isFalse()
    }

}