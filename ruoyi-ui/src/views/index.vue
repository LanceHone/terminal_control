<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">

        
        <h2>工控网络隔离装置iGAP-GW</h2>
       <p>
        <b>制造企业：</b>国家管网集团北京管道有限公司<br />
        <b>产品名：</b>工控网络隔离装置iGAP-GW<br />
        <b>版本：</b>{{ version }}<br />
        <!-- <b>产品类型：</b>网络隔离装置<br />
        <b>产品型号：</b>iGAP-GW<br />
        <b>产品编号：</b>iGAP-GW-4.5.02<br />
        <b>发布日期：</b>2023年10月10日<br />
        <b>产品网址：</b><a href="https://www.bjpipe.com/" target="_blank">https://www.b -->
       </p>
      </el-col>

    </el-row>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "4.5.02",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

