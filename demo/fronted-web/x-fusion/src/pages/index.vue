<template>
  <v-container>
    <v-row>
      <v-col v-for="(component, i) in mfConfig.components" :key="i" cols="auto">
        <v-btn @click="openDialog(component)">
          {{ component.meta || component.component }}
        </v-btn>
      </v-col>
    </v-row>
  </v-container>

  <v-dialog v-model="dialog" max-width="80vw">
    <v-card>
      <v-card-title>
        <span class="headline">{{ selectedComponent?.meta || selectedComponent?.component }}</span>
      </v-card-title>
      <v-card-text>
        <plugin-view v-if="selectedComponent" :plugin-name="selectedComponent.component" />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn color="blue darken-1" text @click="dialog = false">
          Close
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
  import type { PluginComponent } from '@/types/module-federation.ts'
  import { ref } from 'vue'
  import { useModuleFederation } from '@/stores/module-federation.ts'

  const mfConfig = useModuleFederation().mfConfig

  const dialog = ref(false)
  const selectedComponent = ref<PluginComponent | null>(null)

  const openDialog = (component: PluginComponent) => {
    selectedComponent.value = component
    dialog.value = true
  }
</script>
