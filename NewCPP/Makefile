.PHONY: test_obj_exe_run test_obj_cuda_run clean

GCC = gcc
NVCC = nvcc
CUDA_FLAGS = -arch=sm_86

test_obj_exe_run:
	@$(GCC) -c Node.h ISCALA.h DFS.h ER_Graph.h Read_Graph.h Write_Graph.h Driver.cpp -o Node ISCALA DFS ER_Graph Read_Graph Write_Graph Driver
	@./Driver

test_obj_cuda_run: 
	@$(NVCC) $(CUDA_FLAGS) -o DriverCuda Driver.cu
	@./DriverCuda

#rm -f test test2 test_cu *.o this is for Linux
#del test test2 test_cu *.exe *.o this is for Windows
clean:
	rm -f *.exe *.cu *.o