.PHONY: test_obj_exe_run test_obj_cuda_run clean

GCC = g++
NVCC = nvcc
CUDA_FLAGS = -arch=sm_86

test_obj_exe_run:
					$(GCC) -c Node.h ISCALA.h DFS.h ER_Graph.h Read_Graph.h Write_Graph.h Driver.cpp
					$(GCC) -o Node ISCALA DFS ER_Graph Read_Graph Write_Graph Driver
					./Driver

test_obj_cuda_run: 
					$(NVCC) $(CUDA_FLAGS) -o DriverCuda Driver.cu
					./DriverCuda

clean:
		rm -f *.exe *.cu *.o Driver DriverCuda