<template>
  <div class="directory-selector">
    <el-input
        :value="directoryPath"
        placeholder="请选择目录"
        readonly
        class="clickable-input"
        @click.native="openDirectoryDialog"
    >
      <template #append>
        <el-button
            icon="el-icon-folder"
            @click="openDirectoryDialog"
        ></el-button>
      </template>
    </el-input>

    <el-dialog
        :visible.sync="dialogVisible"
        :modal="false"
        title="选择目录"
        width="600px"
        class="directory-dialog"
    >
      <div class="tree-container">
        <el-tree
            ref="directoryTree"
            :data="directoryData"
            :props="treeProps"
            node-key="path"
            lazy
            :load="loadNode"
            :expand-on-click-node="false"
        >
          <span class="custom-node" slot-scope="{ node, data }">
            <el-radio
                v-model="selectedPath"
                :label="data.path"
                :disabled="!data.readable"
            >
              <span class="dir-label"
                    @click.stop="handleLabelClick(data)"
                    @dblclick="handleNodeDoubleClick(data,node)"
              >
                {{ node.label }}
              </span>
            </el-radio>
          </span>
        </el-tree>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSelection">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {directoryTree} from "@/api/common/directory";
import {StringUtils} from "@/utils/StringUtils";

export default {
  model: {
    prop: 'value',
    event: 'input'
  },
  props: {
    value: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      directoryPath: this.value,
      dialogVisible: false,
      directoryData: [],
      treeProps: {
        children: "children",
        label: "name"
      },
      selectedPath: "",
      directoryCache: new Map()
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(newVal) {
        this.directoryPath = newVal;
        this.selectedPath = newVal;
      }
    }
  },
  methods: {
    /**
     * 打开目录选择对话框
     */
    openDirectoryDialog() {
      let that = this;
      this.dialogVisible = true;
      let pathLabels = StringUtils.splitAndTrim(this.directoryPath, '/\\');
      let pathSeperator = "/";
      let currentLevelPath = "/";
      if (this.directoryData.length === 0) {
        this.loadNode({level: 0}, (children) => {
          that.directoryData = children;
          currentLevelPath = currentLevelPath + pathLabels[0];
          that.autoLoadNode({level: 1, data: {path: currentLevelPath}}, pathLabels);
          // this.loadNode({ level: 1, data: {path: currentLevelPath} }, (children) => {
        });
      }
    },
    /**
     * 初始加载树层
     * @param node
     * @param paths
     */
    autoLoadNode(node, paths) {
      let that = this;
      this.loadNode(node, (children) => {
        if (children.length > 0) {
          let pathLabel = paths[node.level];
          let currentLevelPath = node.data.path + "/" + pathLabel;
          if (node.level < paths.length - 1) {
            that.autoLoadNode({level: node.level + 1, data: {path: currentLevelPath}}, paths);
          } else if (node.level == paths.length) {

          }

          // let level = node.level + 1;

          // this.$refs.tree.setCurrentKey(children[0].path);
          // this.$refs.tree.store.nodesMap[children[0].path].expanded = true;
          // this.$refs.tree.store.nodesMap[children[0].path].expanded = false;
          // this.$refs.tree.store.nodesMap[children[0].path].expanded = true;
        }
      });
    },

    /**
     * 加载树节点
     * @param node
     * @param resolve
     * @returns {*}
     */
    loadNode(node, resolve) {
      if (node.level > 0 && !node.data.readable) {
        return;
      }
      const path = node.level === 0 ? "/" : node.data.path;
      if (this.directoryCache.has(path)) {
        return resolve(this.directoryCache.get(path));
      }

      directoryTree({path}).then(response => {
        const children = response.data.map(child => ({
          ...child,
          children: child.hasChildren ? [] : null
        }));
        this.directoryCache.set(path, children);
        resolve(children);
      });
    },

    handleLabelClick(data) {
      if (!data.readable) return;
      this.selectedPath = data.path;
    },

    // 处理节点双击
    handleNodeDoubleClick(data, node, component, event) {
      if (!data.readable) return;
      // console.log('双击事件触发',data,node);

      // 取消选中之前的目录选择
      this.selectedPath = data.path;
      this.confirmSelection(); // 确认选择并关闭对话框
    },

    confirmSelection() {
      if (this.selectedPath) {
        this.directoryPath = this.selectedPath;
        this.$emit('input', this.selectedPath);
        this.$emit('change', this.selectedPath);
      }
      this.dialogVisible = false;
    }
  }
};
</script>

<style scoped>
.clickable-input ::v-deep .el-input__inner {
  cursor: pointer !important;
}

.tree-container {
  height: 60vh;
  overflow-y: auto;
}

.custom-node ::v-deep .el-radio {
  width: 100%;
  height: 26px;
}

.dir-label {
  display: inline-block;
  width: calc(100% - 30px);
  cursor: pointer;
}

::v-deep .el-tree-node__content {
  padding: 5px 0;
}

::v-deep .el-radio__label {
  display: inline !important;
  padding-left: 8px;
}
</style>
