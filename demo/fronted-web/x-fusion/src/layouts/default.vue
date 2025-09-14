<template>
  <v-layout class="rounded rounded-md border">
    <v-navigation-drawer
      v-model="open"
    >
      <v-list>
        <v-list-item
          prepend-avatar="@/assets/logo.png"
          title="yx"
        />
      </v-list>

      <v-divider/>

      <v-list density="compact" nav>
        <v-list-item
          title="首页"
          value="首页"
          @click="$router.push('/')"
        />
<!--        <v-list-item-->
<!--          v-for="menu in mfConfig.menus"-->
<!--          :key="menu.name"-->
<!--          :title="menu.name"-->
<!--          :value="menu.name"-->
<!--          @click="$router.push(menu.path)"-->
<!--        />-->
        <template v-for="menu in mfConfig.menus">
          <template v-if="menu.children && menu.children.length">
            <v-list-group
              :key="menu.name + '-group'"
            >
              <template v-slot:activator="{ props }">
                <v-list-item
                  v-bind="props"
                  :title="menu.name"
                ></v-list-item>
              </template>
              <v-list-item
                v-for="child in menu.children"
                :key="child.name + '-item'"
                :title="child.name"
                :value="child.name"
                @click="$router.push(child.path)"
              />
            </v-list-group>
          </template>
          <template v-else>
            <v-list-item
              :key="menu.name + '-item'"
              :title="menu.name"
              :value="menu.name"
              @click="$router.push(menu.path)"
            />
          </template>
        </template>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar :elevation="0">
      <template #prepend>
        <v-app-bar-nav-icon @click.stop="open = !open" v-if="mobile"/>
      </template>

      <!--      <v-app-bar-title>Application Bar</v-app-bar-title>-->
      <template #append>
        <toggle-icon-button
          density="compact"
          :icons="['mdi-weather-night', 'mdi-white-balance-sunny']"
          style="margin: 1em;"
          @click="theme.toggle()"
        />
        <v-text-field
          append-inner-icon="mdi-magnify"
          density="compact"
          hide-details
          label="搜索"
          single-line
          style="width: 10em;margin-right: 1em;"
          variant="solo"
        />
      </template>
    </v-app-bar>

    <v-main>
      <RouterView v-slot="{ Component, route }">
        <component :is="Component" :key="route.fullPath"/>
      </RouterView>
    </v-main>
  </v-layout>
</template>

<script lang="ts" setup>
import {useTheme} from 'vuetify'
import {useModuleFederation} from '@/stores/module-federation.ts'
import {useDisplay} from 'vuetify'
import '@/styles/global.scss'

const open = ref(true)
const theme = useTheme()
const {mobile} = useDisplay()

const mfConfig = useModuleFederation().mfConfig
</script>
