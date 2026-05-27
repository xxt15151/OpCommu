<template>
  <button 
    :class="['btn', `btn-${variant}`]"
    :disabled="disabled"
    @click="$emit('click')"
  >
    <slot />
  </button>
</template>

<script setup>
defineProps({
  variant: {
    type: String,
    default: 'primary',
    validator: (value) => ['primary', 'secondary', 'secondary-on-dark', 'text-link'].includes(value)
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

defineEmits(['click'])
</script>

<style scoped>
.btn {
  font-family: var(--font-family);
  font-size: var(--text-button);
  font-weight: var(--font-bold);
  letter-spacing: 0.5px;
  padding: 12px 32px;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  border-radius: var(--radius-none);
  height: 48px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.btn-primary {
  background: var(--primary);
  color: var(--on-primary);
}

.btn-primary:hover:not(:disabled) {
  background: var(--primary-active);
}

.btn-secondary {
  background: var(--canvas);
  color: var(--ink);
  border: 1px solid var(--hairline-strong);
}

.btn-secondary:hover:not(:disabled) {
  border-color: var(--ink);
}

.btn-secondary-on-dark {
  background: transparent;
  color: var(--on-dark);
  border: 1px solid var(--on-dark);
}

.btn-secondary-on-dark:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.1);
}

.btn-text-link {
  background: none;
  color: var(--ink);
  padding: 0;
  height: auto;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  font-size: var(--text-label-uppercase);
}

.btn-text-link:hover {
  color: var(--primary);
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>