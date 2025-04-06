<template>
  <div class="check-access">
    <h1>检查访问请求</h1>
    <form @submit.prevent="doCheckAccess">
      <div class="form-group">
        <label for="deviceId">设备ID:</label>
        <input type="number" id="deviceId" v-model.number="formData.deviceId" required />
      </div>
      <div class="form-group">
        <label for="functionCode">功能码:</label>
        <input type="text" id="functionCode" v-model="formData.functionCode" required />
      </div>
      <div class="form-group">
        <label for="rw">读写类型:</label>
        <input type="text" id="rw" v-model="formData.rw" required />
      </div>
      <div class="form-group">
        <label for="registerAddress">寄存器地址:</label>
        <input type="number" id="registerAddress" v-model.number="formData.registerAddress" required />
      </div>
      <div class="form-group">
        <label for="valueRange">控制值范围:</label>
        <input type="number" id="valueRange" v-model.number="formData.valueRange" required />
      </div>
      <button type="submit">检查</button>
    </form>
    <div v-if="result" class="result">
      <p>{{ result }}</p>
    </div>
  </div>
</template>

<script>
import { checkAccess } from '@/api/access/indu/modbus'


export default {
  data() {
    return {
      formData: {
        deviceId: null,
        functionCode: '',
        rw: '',
        registerAddress: null,
        valueRange: null,
      },
      result: '',
    };
  },
  methods: {
     doCheckAccess() {
console.log("doCheckAccess");
       checkAccess(this.formData).then(response => {
         this.result = response.data
         this.$message(response.data.result)
       })
    },
  },
};
</script>

<style>
.check-access {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.result {
  margin-top: 20px;
  padding: 10px;
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 4px;
}
</style>
